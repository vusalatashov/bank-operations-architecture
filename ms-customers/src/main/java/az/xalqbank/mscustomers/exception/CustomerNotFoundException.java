package az.xalqbank.mscustomers.exception;

/**
 * Custom exception thrown when a customer is not found.
 */
public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
