package com.expert.beaute.mobile_backend.file;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("images")
@RequiredArgsConstructor
public class FileStorageController {
    private static final Logger log = LoggerFactory.getLogger(FileStorageController.class);
    private final FirebaseStorageService firebaseStorageService;

    /**
     * Endpoint pour récupérer une image
     * @param fileName Le nom du fichier
     * @param userId L'ID de l'utilisateur
     * @return L'URL signée pour accéder au fichier
     */
    @GetMapping("/{userId}/{fileName:.+}")
    public ResponseEntity<String> getFile(
            @PathVariable String userId,
            @PathVariable String fileName) {

        log.info("Requesting file: {} for user: {}", fileName, userId);
        String filePath = userId + "/" + fileName;
        String signedUrl = firebaseStorageService.generateSignedUrl(filePath);

        if (signedUrl != null) {
            return ResponseEntity.ok(signedUrl);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Endpoint pour uploader une image
     * @param file Le fichier à uploader
     * @param userId L'ID de l'utilisateur
     * @return Le chemin du fichier uploadé
     */
    @PostMapping("/{userId}")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable String userId) {
        try {
            String filePath = firebaseStorageService.storeFile(file, userId);
            return ResponseEntity.ok(filePath);
        } catch (Exception e) {
            log.error("Error uploading file for user {}: {}", userId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
