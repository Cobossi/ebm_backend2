package com.expert.beaute.mobile_backend.expert.login;

import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.expert.ExpertRepository;
import com.expert.beaute.mobile_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginEService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ExpertRepository expertRepository;


    public LoginEResponse login (LoginERequest request) {
        try {
            // Recherche du client par email ou numéro de téléphone
            Optional<Expert> clientOpt;
            String identifier;

            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                identifier = request.getEmail();
                clientOpt = expertRepository.findByEmail(identifier);
            } else if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
                identifier = request.getPhoneNumber();
                clientOpt = expertRepository.findByPhoneNumber(identifier);
            } else {
                return buildErrorResponse("Email ou numéro de téléphone requis");
            }

            if (clientOpt.isEmpty()) {
                return buildErrorResponse("Compte non trouvé");
            }

            Expert expert = clientOpt.get();

            // Vérification si le compte est activé
            if (!expert.isEnabled()) {
                return buildErrorResponse("Compte non activé. Veuillez activer votre compte");
            }

            // Vérification si le compte est bloqué
            if (expert.isAccountLocked()) {
                return buildErrorResponse("Compte bloqué. Veuillez contacter l'administrateur");
            }

            // Vérification directe du mot de passe
            if (!passwordEncoder.matches(request.getPassword(), expert.getPassword())) {
                return buildErrorResponse("Mot de passe incorrect");
            }

            // Si l'authentification réussit, générer le token
            var claims = new HashMap<String, Object>();
            claims.put("fullName", expert.getFullName());

            var jwtToken = jwtService.generateToken(claims, expert);

            return LoginEResponse.builder()
                    .token(jwtToken)
                    .Id_client(expert.getId_expert())
                    .success(true)
                    .message("Connexion réussie")
                    .build();

        } catch (Exception e) {
            return buildErrorResponse("Une erreur est survenue lors de la connexion: " + e.getMessage());
        }
    }

    private LoginEResponse buildErrorResponse(String message) {
        return LoginEResponse.builder()
                .success(false)
                .message(message)
                .build();
    }
}