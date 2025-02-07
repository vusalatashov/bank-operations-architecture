package az.xalqbank.msphotostorage.util;

public class FileUtils {

    /**
     * Dosya adından uzantıyı döner.
     *
     * @param fileName İşlenen dosya adı.
     * @return Dosya uzantısı veya boş String.
     */
    public static String getFileExtension(String fileName) {
        if (fileName != null && fileName.lastIndexOf('.') != -1) {
            return fileName.substring(fileName.lastIndexOf('.') + 1);
        }
        return "";
    }
}
