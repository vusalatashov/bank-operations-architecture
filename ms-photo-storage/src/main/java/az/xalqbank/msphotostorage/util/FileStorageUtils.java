package az.xalqbank.msphotostorage.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileStorageUtils {

    private static final String UPLOAD_DIR = "ms-photo-storage/src/main/resources/uploads"; // Faylların saxlanacağı direktoriyanı dəyişə bilərsiniz

    public static String saveFileToLocal(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        return saveFile(UPLOAD_DIR, fileName, file.getInputStream());
    }

    public static String saveFile(String uploadDir, String fileName, InputStream fileContent) throws IOException {
        File uploadDirectory = new File(uploadDir);

        // Qovluq yoxdursa, yarat
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs(); // Bütün parent qovluqları da yaradır
        }

        Path filePath = Path.of(uploadDir, fileName);
        Files.copy(fileContent, filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toAbsolutePath().toString(); // Faylın tam yolunu qaytar
    }
}
