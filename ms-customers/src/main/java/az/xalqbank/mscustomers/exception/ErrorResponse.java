package az.xalqbank.mscustomers.exception;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Custom response structure for errors.
 */
@Data
public class ErrorResponse {

    private LocalDateTime timestamp;
    private String message;
    private String details;

    public ErrorResponse(LocalDateTime timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}
