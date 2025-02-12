package az.xalqbank.msfileupload.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Utility class for file storage operations.
 */
public class FileStorageUtils {

    private static final String UPLOAD_DIR = "ms-photo-storage/src/main/resources/uploads";

    /**
     * Saves the provided file to local storage and returns the file URL.
     *
     * @param file the multipart file.
     * @return the absolute path of the stored file.
     * @throws IOException if file storage fails.
     */
    public static String saveFileToLocal(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        return saveFile(UPLOAD_DIR, fileName, file.getInputStream());
    }

    /**
     * Saves the file content to the specified directory with the given file name.
     *
     * @param uploadDir   the directory to upload the file.
     * @param fileName    the file name.
     * @param fileContent the input stream of the file.
     * @return the absolute path of the stored file.
     * @throws IOException if file storage fails.
     */
    public static String saveFile(String uploadDir, String fileName, InputStream fileContent) throws IOException {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        Path filePath = Path.of(uploadDir, fileName);
        Files.copy(fileContent, filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toAbsolutePath().toString();
    }
}
