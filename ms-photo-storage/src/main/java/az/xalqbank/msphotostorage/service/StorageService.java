package az.xalqbank.msphotostorage.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {

    // Yüklenen dosyaların saklanacağı dizin
    private final Path rootLocation = Paths.get("uploads");

    public StorageService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    /**
     * Dosyayı belirtilen dizine kaydeder ve dosyanın saklandığı yol bilgisini döner.
     *
     * @param file     Yüklenecek dosya.
     * @param fileName Saklanacak dosya adı.
     * @return Dosyanın tam yol bilgisini içeren String.
     * @throws IOException
     */
    public String store(MultipartFile file, String fileName) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file");
        }
        Path destinationFile = this.rootLocation.resolve(Paths.get(fileName))
                .normalize().toAbsolutePath();
        Files.copy(file.getInputStream(), destinationFile);
        // Gerçek uygulamada, dosya erişimi için bir URL üretilebilir.
        return destinationFile.toString();
    }
}
