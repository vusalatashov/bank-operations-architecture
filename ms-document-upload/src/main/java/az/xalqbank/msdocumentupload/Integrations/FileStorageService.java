package az.xalqbank.msdocumentupload.Integrations;

import org.springframework.web.multipart.MultipartFile;

/**
 * Service for storing files (either locally or on cloud storage).
 */
public interface FileStorageService {
    /**
     * Stores the file and returns the URL to access it.
     *
     * @param file the file to store.
     * @param fileName the name to assign to the stored file.
     * @return the URL of the stored file.
     */
    String storeFile(MultipartFile file, String fileName);
}
