package com.example.portfoliobackendapp.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("C:/Users/Alper/Desktop/PortfolioBackendLast/PortfolioBackendapp/user-images");

    public String storeFile(MultipartFile file, String username) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID() + fileExtension;

        Path userDir = this.rootLocation.resolve(username);
        if (!Files.exists(userDir)) {
            Files.createDirectories(userDir);
        }

        Path targetLocation = userDir.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), targetLocation);

        return username + "/" + uniqueFileName;
    }
}