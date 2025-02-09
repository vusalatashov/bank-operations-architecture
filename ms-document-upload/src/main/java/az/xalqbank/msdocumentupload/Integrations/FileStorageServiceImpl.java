package az.xalqbank.msdocumentupload.Integrations.impl;

import az.xalqbank.msdocumentupload.Integrations.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.storage.path}")
    private String storageLocation;

    @Override
    public String storeFile(MultipartFile file, String fileName) {
        try {
            Path path = Paths.get(storageLocation + "/" + fileName);
            Files.copy(file.getInputStream(), path);
            return path.toString();  // You can return a URL here if you're using cloud storage.
        } catch (IOException e) {
            throw new RuntimeException("Could not store file: " + fileName, e);
        }
    }
}
