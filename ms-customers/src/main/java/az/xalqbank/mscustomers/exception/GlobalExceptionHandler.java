package az.xalqbank.mscustomers.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Global exception handler for all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle CustomerNotFoundException globally.
     *
     * @param ex The exception that was thrown
     * @return ResponseEntity with the exception message and 404 status
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        logger.error("Customer not found: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                "Customer not found in the system"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle ConstraintViolationException (validation errors) globally.
     *
     * @param ex The exception that was thrown
     * @return ResponseEntity with the validation error details and 400 status
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        String errorMessage = violations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        logger.error("Validation failed: {}", errorMessage);

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                "Validation failed for request",
                errorMessage
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handle any other exceptions globally.
     *
     * @param ex The exception that was thrown
     * @return ResponseEntity with the exception message and 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                "An unexpected error occurred",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
