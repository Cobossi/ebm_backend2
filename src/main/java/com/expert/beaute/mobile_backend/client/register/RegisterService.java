package com.expert.beaute.mobile_backend.client.register;

import com.expert.beaute.mobile_backend.client.*;
import com.expert.beaute.mobile_backend.email.EmailService;
import com.expert.beaute.mobile_backend.email.EmailTemplateName;
import com.expert.beaute.mobile_backend.security.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterService {
    private String activationUrl;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final TokenClientRepository tokenClientRepository;
    private final ClientRepository clientRepository;

    @Autowired
    @Qualifier("userAuthenticationProvider")
    private AuthenticationProvider clientAuthenticationProvider;

    @Transactional
    public void register(RegisterRequest request) throws MessagingException {
        // Validation qu'au moins un moyen de contact est fourni
        if ((request.getEmail() == null || request.getEmail().trim().isEmpty()) &&
                (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty())) {
            throw new RuntimeException("Email ou numéro de téléphone requis");
        }

        // Vérification de l'unicité de l'email si fourni
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            if (clientRepository.existsByEmailAndEnabled(request.getEmail(), true)) {
                throw new EmailAlreadyExistsException("Cet email est déjà utilisé");
            }
        }

        // Vérification de l'unicité du numéro de téléphone si fourni
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().trim().isEmpty()) {
            if (clientRepository.existsByPhoneNumberAndEnabled(request.getPhoneNumber(), true)) {
                throw new PhoneNumberAlreadyExistsException("Ce numéro de téléphone est déjà utilisé");
            }
        }
        // Nettoyer les anciennes inscriptions non activées si elles existent
        cleanupUnactivatedAccounts(request.getEmail(), request.getPhoneNumber());



        // Création du nouveau client
        var client = Client.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .accountLocked(false)
                .build();

        try {
            clientRepository.save(client);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du client", e);
        }

        // Envoi du token d'activation
        try {
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                sendValidationEmail(client);
            } else {
                sendValidationSMS(client);
            }
        } catch (MessagingException e) {
            // En cas d'erreur d'envoi, on supprime le client créé
            tokenClientRepository.deleteAllByClient(client);
            clientRepository.delete(client);
            throw e;
        }
    }


    private void sendValidationEmail(Client client) throws MessagingException {
        var token = generateAndSaveActivationToken(client);
        emailService.sendEmail(
                client.getEmail(),
                client.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                token,
                "Account activation"
        );
    }

    private void sendValidationSMS(Client client) {
        var token = generateAndSaveActivationToken(client);
        // Implémentez ici la logique d'envoi de SMS
        //smsService.sendSMS(client.getPhoneNumber(),
                //"Your activation code is: " + token);
    }
    private String generateAndSaveActivationToken(Client client) {
        // Generate a token
        String generatedToken = generateActivationCode(6);
        var token = TokenClient.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .client(client)
                .build();
        tokenClientRepository.save(token);

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
        TokenClient savedToken = tokenClientRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        Client client = savedToken.getClient();

        // Vérifier si le compte n'est pas déjà activé
        if (client.isEnabled()) {
            throw new RuntimeException("This account is already activated");
        }

        // Vérifier si le délai d'activation n'est pas dépassé
        if (LocalDateTime.now().isAfter(client.getActivationDeadline())) {
            clientRepository.delete(client); // Supprimer le compte non activé
            throw new RuntimeException("Activation period has expired. Please register again");
        }

        // Activer le compte
        client.setEnabled(true);
        clientRepository.save(client);

        // Marquer le token comme utilisé et le sauvegarder pour l'historique
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenClientRepository.save(savedToken);
    }
    @Scheduled(cron = "0 0 * * * *") // Toutes les heures
    @Transactional
    public void cleanupExpiredAccounts() {
        LocalDateTime now = LocalDateTime.now();
        clientRepository.deleteAllByEnabledFalseAndActivationDeadlineBefore(now);
    }

    @Transactional
    public void cleanupUnactivatedAccounts(String email, String phoneNumber) {
        if (email != null && !email.trim().isEmpty()) {
            List<Client> clientsToDelete = clientRepository.findAllByEmailAndEnabledFalse(email);
            for (Client client : clientsToDelete) {
                // Supprimer d'abord les tokens
                tokenClientRepository.deleteAllByClient(client);
                // Puis supprimer le client
                clientRepository.delete(client);
            }
        }

        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            List<Client> clientsToDelete = clientRepository.findAllByPhoneNumberAndEnabledFalse(phoneNumber);
            for (Client client : clientsToDelete) {
                // Supprimer d'abord les tokens
                tokenClientRepository.deleteAllByClient(client);
                // Puis supprimer le client
                clientRepository.delete(client);
            }
        }
    }

}
