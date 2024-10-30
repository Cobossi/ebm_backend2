package com.expert.beaute.mobile_backend.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.InputStream;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.core.io.ClassPathResource;

@Slf4j
//@RequiredArgsConstructor
@Service
//@ConfigurationProperties(prefix = "file")
public class FileStorageServices {

    // @Value("${file.upload-dir}")
    @Getter
    private final  String uploadDir="./uploads";

    private final String defaultImagePath = "./profile.png";

    //@Value("${file.base-url}")
    private final  String baseUrl="http://192.168.1.172/api/v1/images";



    public String storeFile(MultipartFile file) throws IOException {
        // Créer le dossier de destination si nécessaire

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générer un nom de fichier unique
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Stocker le fichier sur le serveur
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName; // Retourne seulement le nom du fichier
    }
    public String getFileUrl(String fileName) {
        return baseUrl + "/" + fileName;
    }


    public String storeDefaultImage() throws IOException {
        String fileName = UUID.randomUUID().toString() + "_default-profile.png";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);

        // Copier l'image par défaut depuis les ressources
        try (InputStream inputStream = new ClassPathResource(defaultImagePath).getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){}

        return fileName;
    }
}

