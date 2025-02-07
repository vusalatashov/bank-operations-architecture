package az.xalqbank.mscustomers.util;

import java.io.File;

public class FileUtils {

    public static String generateFileUrl(String fileName) {
        return "http://localhost:8080/files/" + fileName;
    }

    public static boolean isValidFileSize(File file, long maxSize) {
        return file.length() <= maxSize;
    }

    public static boolean isValidFileType(String fileName) {
        String[] allowedExtensions = {"jpg", "jpeg", "png"};
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        for (String ext : allowedExtensions) {
            if (ext.equalsIgnoreCase(fileExtension)) {
                return true;
            }
        }
        return false;
    }
}
