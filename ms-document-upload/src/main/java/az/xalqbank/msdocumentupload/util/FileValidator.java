package az.xalqbank.msdocumentupload.util;

import org.springframework.web.multipart.MultipartFile;

/**
 * Utility class for validating files.
 */
public class FileValidator {

    private static final long MAX_FILE_SIZE = 10485760; // 10 MB in bytes

    /**
     * Validates the file based on its type and size.
     *
     * @param file the file to validate.
     * @param fileSize the size of the file.
     * @return true if valid, false otherwise.
     */
    public boolean validateFile(MultipartFile file, long fileSize) {
        // Check file type and size
        String fileType = file.getContentType();
        if (!fileType.equals("application/pdf") && !fileType.equals("image/jpeg")) {
            return false;  // Only PDF and JPEG files allowed
        }
        return fileSize <= MAX_FILE_SIZE;
    }
}
