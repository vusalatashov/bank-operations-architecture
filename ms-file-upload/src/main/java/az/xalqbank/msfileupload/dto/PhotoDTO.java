package az.xalqbank.msfileupload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Photo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDTO {
    private Long id;
    private Long customerId;
    private String fileName;
    private String fileUrl;
}
