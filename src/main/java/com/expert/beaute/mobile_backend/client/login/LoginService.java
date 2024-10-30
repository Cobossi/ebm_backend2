package com.expert.beaute.mobile_backend.client.login;

import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.client.ClientRepository;
import com.expert.beaute.mobile_backend.security.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ClientRepository clientRepository;


    public LoginResponse login (LoginRequest request) {
        try {
            // Recherche du client par email ou numéro de téléphone
            Optional<Client> clientOpt;
            String identifier;

            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                identifier = request.getEmail();
                clientOpt = clientRepository.findByEmail(identifier);
            } else if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
                identifier = request.getPhoneNumber();
                clientOpt = clientRepository.findByPhoneNumber(identifier);
            } else {
                return buildErrorResponse("Email ou numéro de téléphone requis");
            }

            if (clientOpt.isEmpty()) {
                return buildErrorResponse("Compte non trouvé");
            }

            Client client = clientOpt.get();

            // Vérification si le compte est activé
            if (!client.isEnabled()) {
                return buildErrorResponse("Compte non activé. Veuillez activer votre compte");
            }

            // Vérification si le compte est bloqué
            if (client.isAccountLocked()) {
                return buildErrorResponse("Compte bloqué. Veuillez contacter l'administrateur");
            }

            // Vérification directe du mot de passe
            if (!passwordEncoder.matches(request.getPassword(), client.getPassword())) {
                return buildErrorResponse("Mot de passe incorrect");
            }

            // Si l'authentification réussit, générer le token
            var claims = new HashMap<String, Object>();
            claims.put("fullName", client.getFullName());

            var jwtToken = jwtService.generateToken(claims, client);

            return LoginResponse.builder()
                    .token(jwtToken)
                    .Id_client(client.getId_client())
                    .success(true)
                    .message("Connexion réussie")
                    .build();

        } catch (Exception e) {
            return buildErrorResponse("Une erreur est survenue lors de la connexion: " + e.getMessage());
        }
    }

    private LoginResponse buildErrorResponse(String message) {
        return LoginResponse.builder()
                .success(false)
                .message(message)
                .build();
    }
}
