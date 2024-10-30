package com.expert.beaute.mobile_backend.expert.register;


import com.expert.beaute.mobile_backend.email.EmailService;
import com.expert.beaute.mobile_backend.email.EmailTemplateName;
import com.expert.beaute.mobile_backend.expert.*;
import com.expert.beaute.mobile_backend.expert.TokenExpert;
import com.expert.beaute.mobile_backend.expert.TokenExpertRepository;
import com.expert.beaute.mobile_backend.file.FileStorageServices;
import com.expert.beaute.mobile_backend.file.FirebaseStorageService;
import com.expert.beaute.mobile_backend.security.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RegisterEService {
    private String activationUrl;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final TokenExpertRepository tokenExpertRepository;
    private final ExpertRepository expertRepository;
    private final FirebaseStorageService firebaseStorageService;


    @Autowired
    @Qualifier("userAuthenticationProvider")
    private AuthenticationProvider clientAuthenticationProvider;

    @Transactional
    public void register(RegisterERequest request) throws MessagingException, IOException {
        // Validation qu'au moins un moyen de contact est fourni
        if ((request.getEmail() == null || request.getEmail().trim().isEmpty()) &&
                (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty())) {
            throw new RuntimeException("Email ou numéro de téléphone requis");
        }

        // Vérification de l'unicité de l'email si fourni
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            if (expertRepository.existsByEmailAndEnabled(request.getEmail(), true)) {
                throw new EmailAlreadyExistsException("Cet email est déjà utilisé");
            }
        }

        // Vérification de l'unicité du numéro de téléphone si fourni
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().trim().isEmpty()) {
            if (expertRepository.existsByPhoneNumberAndEnabled(request.getPhoneNumber(), true)) {
                throw new PhoneNumberAlreadyExistsException("Ce numéro de téléphone est déjà utilisé");
            }
        }

        // Création du nouveau client
        var expert = Expert.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .actus("Bienvenue Chez Ebm")
                .etoile(3.0)
                .expertname(request.getNom() + " " + request.getPrenom())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .accountLocked(false)
                .build();


        // Sauvegarder l'expert pour obtenir son ID
        try {
            expert = expertRepository.save(expert);  // Récupérer l'instance sauvegardée avec l'ID

            // Stocker l'image par défaut avec l'ID de l'expert
            String defaultImagePath = firebaseStorageService.storeDefaultImage(expert.getId_expert().toString());

            // Mettre à jour le chemin de la photo de profil
            expert.setProfilePhoto(defaultImagePath);
            expertRepository.save(expert);  // Sauvegarder à nouveau avec le chemin de la photo

            log.info("Expert enregistré avec succès avec l'image par défaut: {}", defaultImagePath);
        } catch (IOException e) {
            log.error("Erreur lors du stockage de l'image par défaut pour l'expert: {}", expert.getId_expert(), e);
            throw new RuntimeException("Erreur lors du stockage de l'image par défaut", e);
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement de l'expert", e);
            throw new RuntimeException("Erreur lors de l'enregistrement de l'expert", e);
        }
            try {
            expertRepository.save(expert);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de l'expert", e);
        }

        // Envoi du token d'activation
        try {
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                sendValidationEmail(expert);
            } else {
                sendValidationSMS(expert);
            }
        } catch (MessagingException e) {
            // En cas d'erreur d'envoi, on supprime l'expert créé
            tokenExpertRepository.deleteAllByExpert(expert);
            expertRepository.delete(expert);
            throw e;
        }
    }


    private void sendValidationEmail(Expert expert) throws MessagingException {
        var token = generateAndSaveActivationToken(expert);
        emailService.sendEmail(
                expert.getEmail(),
                expert.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                token,
                "Account activation"
        );
    }

    private void sendValidationSMS(Expert expert) {
        var token = generateAndSaveActivationToken(expert);
        // Implémentez ici la logique d'envoi de SMS
        //smsService.sendSMS(client.getPhoneNumber(),
        //"Your activation code is: " + token);
    }
    private String generateAndSaveActivationToken(Expert expert) {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        var token = TokenExpert.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .expert(expert)
                .build();
        tokenExpertRepository.save(token);

        return generatedToken;
    }
    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        TokenExpert savedToken = tokenExpertRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        Expert expert = savedToken.getExpert();

        // Vérifier si le compte n'est pas déjà activé
        if (expert.isEnabled()) {
            throw new RuntimeException("This account is already activated");
        }

        // Vérifier si le délai d'activation n'est pas dépassé
        if (LocalDateTime.now().isAfter(expert.getActivationDeadline())) {
            expertRepository.delete(expert); // Supprimer le compte non activé
            throw new RuntimeException("Activation period has expired. Please register again");
        }

        // Activer le compte
        expert.setEnabled(true);
        expertRepository.save(expert);

        // Marquer le token comme utilisé et le sauvegarder pour l'historique
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenExpertRepository.save(savedToken);
    }
    @Scheduled(cron = "0 0 * * * *") // Toutes les heures
    @Transactional
    public void cleanupExpiredAccounts() {
        LocalDateTime now = LocalDateTime.now();
        expertRepository.deleteAllByEnabledFalseAndActivationDeadlineBefore(now);
    }

    @Transactional
    public void cleanupUnactivatedAccounts(String email, String phoneNumber) {
        if (email != null && !email.trim().isEmpty()) {
            List<Expert> clientsToDelete = expertRepository.findAllByEmailAndEnabledFalse(email);
            for (Expert expert : clientsToDelete) {
                // Supprimer d'abord les tokens
                tokenExpertRepository.deleteAllByExpert(expert);
                // Puis supprimer le client
                expertRepository.delete(expert);
            }
        }

        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            List<Expert> clientsToDelete = expertRepository.findAllByPhoneNumberAndEnabledFalse(phoneNumber);
            for (Expert expert : clientsToDelete) {
                // Supprimer d'abord les tokens
                tokenExpertRepository.deleteAllByExpert(expert);
                // Puis supprimer le client
                expertRepository.delete(expert);
            }
        }
    }

}