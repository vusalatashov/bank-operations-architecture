package az.xalqbank.msfileupload.exception;

/**
 * Custom exception for photo storage operations.
 */
public class PhotoStorageException extends RuntimeException {

    public PhotoStorageException(String message) {
        super(message);
    }

    public PhotoStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
