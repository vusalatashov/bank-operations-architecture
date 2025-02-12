package az.xalqbank.msfileupload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request DTO for uploading a photo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoUploadRequest {
    private MultipartFile file;
    private String name;
    private String contactDetails;
}
