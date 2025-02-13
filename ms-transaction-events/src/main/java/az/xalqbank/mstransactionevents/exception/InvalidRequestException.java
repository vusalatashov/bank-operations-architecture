package az.xalqbank.mstransactionevents.exception;

/**
 * Exception thrown when a request is invalid.
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
