package az.xalqbank.msfileupload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for photo operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoResponse {
    private String id;
    private String url;
    private String message;
}
