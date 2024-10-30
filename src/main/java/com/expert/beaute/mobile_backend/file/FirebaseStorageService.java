package com.expert.beaute.mobile_backend.file;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirebaseStorageService {

    @Value("${firebase.bucket-name}")
    private String bucketName;

    private final Storage storage;

    // Chemin vers l'image par défaut
    private final String defaultImagePath = "./profile.png";

    @PostConstruct
    public void initialize() {
        // Vérification de l'existence de l'image par défaut
        try {
            Resource resource = new ClassPathResource(defaultImagePath);
            if (!resource.exists()) {
                log.warn("⚠️ L'image de profil par défaut n'est pas trouvée à l'emplacement: {}", defaultImagePath);
            } else {
                log.info("✅ Image de profil par défaut trouvée avec succès");
            }

            // Vérification de l'accès au bucket
            storage.get(bucketName);
            log.info("✅ Connexion au bucket Firebase établie avec succès");
        } catch (Exception e) {
            log.error("❌ Erreur lors de l'initialisation du service: {}", e.getMessage());
            throw new RuntimeException("Erreur d'initialisation", e);
        }
    }


    /**
     * Stocke un fichier dans le dossier spécifique de l'utilisateur
     * @param file Le fichier à stocker
     * @param userId L'ID de l'utilisateur
     * @return L'URL publique du fichier
     */
    public String storeFile(MultipartFile file, String userId) throws IOException {
        // Génération d'un nom unique pour le fichier
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        // Création du chemin complet (userId/fileName)
        String fullPath = userId + "/" + fileName;

        // Configuration du blob avec le type de contenu
        BlobId blobId = BlobId.of(bucketName, fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        // Upload du fichier
        storage.create(blobInfo, file.getBytes());

        log.info("File {} uploaded successfully for user {}", fileName, userId);

        return fullPath;
    }

    /**
     * Génère une URL signée temporaire pour accéder au fichier
     * @param filePath Le chemin du fichier dans Storage
     * @return L'URL signée valide pour une durée limitée
     */
    public String generateSignedUrl(String filePath) {
        BlobId blobId = BlobId.of(bucketName, filePath);
        Blob blob = storage.get(blobId);

        if (blob == null) {
            log.warn("File not found: {}", filePath);
            return null;
        }

        // Génération d'une URL signée valide pendant 1 heure
        URL signedUrl = blob.signUrl(100, TimeUnit.DAYS);
        return signedUrl.toString();
    }

    /**
     * Stocke l'image de profil par défaut pour un nouvel utilisateur
     * @param userId L'ID de l'utilisateur
     * @return Le chemin du fichier stocké
     */
    public String storeDefaultImage(String userId) throws IOException {
        String fileName = "profile.png";
        String fullPath = userId + "/" + fileName;

        // Lecture de l'image par défaut depuis les ressources
        byte[] imageBytes = new ClassPathResource("profile.png").getInputStream().readAllBytes();

        BlobId blobId = BlobId.of(bucketName, fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("image/png")
                .build();

        storage.create(blobInfo, imageBytes);

        log.info("Default profile image created for user {}", userId);

        return fullPath;
    }

    /**
     * Supprime un fichier
     * @param filePath Le chemin du fichier à supprimer
     */
    public void deleteFile(String filePath) {
        BlobId blobId = BlobId.of(bucketName, filePath);
        boolean deleted = storage.delete(blobId);

        if (deleted) {
            log.info("File {} deleted successfully", filePath);
        } else {
            log.warn("File {} not found or could not be deleted", filePath);
        }
    }


}
