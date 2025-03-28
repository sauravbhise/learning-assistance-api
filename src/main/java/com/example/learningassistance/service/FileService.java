package com.example.learningassistance.service;

import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    public static final String UPLOAD_DIR = "uploads/";

    public FileService() throws IOException {
        Files.createDirectories(Paths.get(UPLOAD_DIR));
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (!file.getContentType().equals("application/pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed!");
        }

        Path filePath = Paths.get(UPLOAD_DIR +  file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    public Resource getFile(String filePath) throws MalformedURLException {
        Path path = Paths.get(filePath).normalize();
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            throw new RuntimeException("File not found: " + filePath);
        }
        return resource;
    }
}
