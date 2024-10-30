package com.expert.beaute.mobile_backend.client.update;


import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.client.ClientRepository;
import com.expert.beaute.mobile_backend.client.ClientResponse;
import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.expert.ExpertResponse;
import com.expert.beaute.mobile_backend.file.FirebaseStorageService;
import com.expert.beaute.mobile_backend.prestation.PrestationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateClientService {

    @Autowired
    private ClientRepository clientRepository;

    private final FirebaseStorageService firebaseStorageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void  updatePassword(String clientId, String oldPassword, String newPassword) {
        // Récupérer l'expert
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));

        // Vérifier si l'ancien mot de passe correspond
        if (!passwordEncoder.matches(oldPassword, client.getPassword())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }

        // Encoder et mettre à jour le nouveau mot de passe
        client.setPassword(passwordEncoder.encode(newPassword));
        clientRepository.save(client);


    }
    @Transactional
    public Client updateProfilePhoto(String id, MultipartFile profilePhoto) throws IOException {
        // Récupérer l'expert
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        // Si l'expert a déjà une photo de profil, la supprimer
        if (client.getProfilePhoto() != null && !client.getProfilePhoto().isEmpty()) {
            firebaseStorageService.deleteFile(client.getProfilePhoto());
        }

        // Stocker la nouvelle photo et obtenir son chemin
        String photoPath = firebaseStorageService.storeFile(profilePhoto, id);

        // Mettre à jour l'expert avec le nouveau chemin de la photo
        client.setProfilePhoto(photoPath);

        return clientRepository.save(client);
    }

    public Client updateFirstName(String clientId, String nom) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        client.setNom(nom);
        return clientRepository.save(client);
    }

    public Client updateLastName(String userId, String prenom) {
        Client client = clientRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        client.setPrenom(prenom);
        return clientRepository.save(client);
    }

    public Client updatePhoneNumber(String userId, String newPhoneNumber) {
        Client client = clientRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        client.setPhoneNumber(newPhoneNumber);
        return clientRepository.save(client);
    }

    public Client updateEmail(String userId, String newEmail) {
        Client client = clientRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        client.setEmail(newEmail);
        return clientRepository.save(client);
    }

    public Client getsClientById(String userId) {
        return clientRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public ClientResponse getClientById(String userId) {
        // Récupérer l'expert
        Client client = clientRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));

        // Construire la réponse
        ClientResponse.ClientResponseBuilder responseBuilder = ClientResponse.builder()
                .id_client(client.getId_client())
                .createDate(client.getCreateDate())
                .phoneNumber(client.getPhoneNumber())
                .nom(client.getNom())
                .email(client.getEmail())
                .prenom(client.getPrenom());


        // Gérer la photo de profil
        if (client.getProfilePhoto() != null && !client.getProfilePhoto().isEmpty()) {
            responseBuilder.profilePhoto(client.getProfilePhoto());  // Chemin Firebase original
            String signedUrl = firebaseStorageService.generateSignedUrl(client.getProfilePhoto());
            responseBuilder.profilePhotoUrl(signedUrl);  // URL signée
        }

        return responseBuilder.build();
    }
}
