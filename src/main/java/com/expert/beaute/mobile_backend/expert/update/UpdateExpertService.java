package com.expert.beaute.mobile_backend.expert.update;


import com.expert.beaute.mobile_backend.client.Client;
import com.expert.beaute.mobile_backend.expert.Expert;
import com.expert.beaute.mobile_backend.expert.ExpertRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateExpertService {

    @Autowired
    private ExpertRepository expertRepository;

    private final FirebaseStorageService firebaseStorageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void  updatePassword(String expertId, String oldPassword, String newPassword) {
        // Récupérer l'expert
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));

        // Vérifier si l'ancien mot de passe correspond
        if (!passwordEncoder.matches(oldPassword, expert.getPassword())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }

        // Encoder et mettre à jour le nouveau mot de passe
        expert.setPassword(passwordEncoder.encode(newPassword));
         expertRepository.save(expert);


    }

    @Transactional
    public void updateNom(String id, String nom) {
        Expert expert = expertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expert not found with id: " + id));
        expert.setNom(nom);
       expertRepository.save(expert);
    }
    @Transactional
    public ExpertResponse updateExpertname(String id, String expertname) {
        // Recherche de l'expert
        Expert expert = expertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expert not found with id: " + id));

        // Mise à jour de l'expertname
        expert.setExpertname(expertname);

        // Sauvegarde des modifications
        Expert updatedExpert = expertRepository.save(expert);
        return ExpertResponse.builder()
                .id_expert(updatedExpert.getId_expert())
                .nom(updatedExpert.getNom())
                .prenom(updatedExpert.getPrenom())
                .email(updatedExpert.getEmail())
                .phoneNumber(updatedExpert.getPhoneNumber())
                .expertname(updatedExpert.getExpertname())
                .actus(updatedExpert.getActus())
                .etoile(updatedExpert.getEtoile())
                .build();

    }

    @Transactional
    public void updatePrenom(String id, String prenom) {
        Expert expert = expertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expert not found with id: " + id));
        expert.setPrenom(prenom);
        expertRepository.save(expert);
    }

    @Transactional
    public Expert updateEmail(String id, String email) {
        Expert expert = expertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expert not found with id: " + id));
        expert.setEmail(email);

        return expertRepository.save(expert);
    }

    @Transactional
    public void updatePhoneNumber(String id, String phoneNumber) {
        Expert expert = expertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expert not found with id: " + id));
        expert.setPhoneNumber(phoneNumber);
        expertRepository.save(expert);
    }

    @Transactional
    public void updateActus(String id, String actus) {
        Expert expert = expertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expert not found with id: " + id));
        expert.setActus(actus);
         expertRepository.save(expert);
    }

    @Transactional
    public Expert updateProfilePhoto(String id, MultipartFile profilePhoto) throws IOException {
        // Récupérer l'expert
        Expert expert = expertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expert not found with id: " + id));

        // Si l'expert a déjà une photo de profil, la supprimer
        if (expert.getProfilePhoto() != null && !expert.getProfilePhoto().isEmpty()) {
            firebaseStorageService.deleteFile(expert.getProfilePhoto());
        }

        // Stocker la nouvelle photo et obtenir son chemin
        String photoPath = firebaseStorageService.storeFile(profilePhoto, id);

        // Mettre à jour l'expert avec le nouveau chemin de la photo
        expert.setProfilePhoto(photoPath);

        return expertRepository.save(expert);
    }


    public Expert getExpertReservationId(String id){
        Expert expert=expertRepository.findById(id).orElseThrow(()->new RuntimeException("not found"));
        return  expert;
}
    public ExpertResponse getExpertById(String userId) {
        // Récupérer l'expert
        Expert expert = expertRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));

        // Convertir les prestations
        List<PrestationResponse> prestations = expert.getPrestation().stream()
                .map(service -> PrestationResponse.builder()
                        .id(service.getId())
                        .nom(service.getNom())
                        .prix(expert.getServicePrices().get(service))
                        .build())
                .collect(Collectors.toList());

        // Construire la réponse
        ExpertResponse.ExpertResponseBuilder responseBuilder = ExpertResponse.builder()
                .id_expert(expert.getId_expert())
                .actus(expert.getActus())
                .createDate(expert.getCreateDate())
                .phoneNumber(expert.getPhoneNumber())
                .nom(expert.getNom())
                .email(expert.getEmail())
                .expertname(expert.getExpertname())
                .prenom(expert.getPrenom())
                .etoile(expert.getEtoile())
                .prestation(prestations);

        // Gérer la photo de profil
        if (expert.getProfilePhoto() != null && !expert.getProfilePhoto().isEmpty()) {
            responseBuilder.profilePhoto(expert.getProfilePhoto());  // Chemin Firebase original
            String signedUrl = firebaseStorageService.generateSignedUrl(expert.getProfilePhoto());
            responseBuilder.profilePhotoUrl(signedUrl);  // URL signée
        }

        return responseBuilder.build();
    }

    public List<ExpertResponse> getAllExpertsExceptConnected(String connectedExpertId) {
        // Récupérer tous les experts sauf l'expert connecté
        List<Expert> experts = expertRepository.findByExpertIdNot(connectedExpertId);

        // Convertir la liste d'experts en liste d'ExpertResponse
        return experts.stream()
                .map(expert -> {
                    // Construire la liste des prestations pour chaque expert
                    List<PrestationResponse> prestations = expert.getPrestation().stream()
                            .map(service -> PrestationResponse.builder()
                                    .id(service.getId())
                                    .nom(service.getNom())
                                    .prix(expert.getServicePrices().get(service))
                                    .build())
                            .collect(Collectors.toList());

                    // Construire l'ExpertResponse avec toutes les informations
                    ExpertResponse.ExpertResponseBuilder responseBuilder = ExpertResponse.builder()
                            .id_expert(expert.getId_expert())
                            .actus(expert.getActus())
                            .nom(expert.getNom())
                            .expertname(expert.getExpertname())
                            .prenom(expert.getPrenom())
                            .etoile(expert.getEtoile())
                            .prestation(prestations);
                    // Gérer la photo de profil
                    if (expert.getProfilePhoto() != null && !expert.getProfilePhoto().isEmpty()) {
                        responseBuilder.profilePhoto(expert.getProfilePhoto());  // Chemin Firebase original
                        String signedUrl = firebaseStorageService.generateSignedUrl(expert.getProfilePhoto());
                        responseBuilder.profilePhotoUrl(signedUrl);  // URL signée
                    }

                    return responseBuilder.build();
                })
                .collect(Collectors.toList());
    }
    /*public ExpertResponse getPrestationById(String userId) {
        Expert expert= expertRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));

        return ExpertResponse.builder()
                .id_expert(expert.getId_expert())
                .actus(expert.getActus())
                .nom(expert.getNom())
                .prenom(expert.getPrenom())
                .etoile(expert.getEtoile())
                .profilePhoto(expert.getProfilePhoto())

                .build();
    }*/

    public Expert getExpertClientById(String userId) {
        return expertRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
