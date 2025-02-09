package az.xalqbank.msdocumentupload.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Data Transfer Object for document upload.
 * Contains metadata and the file to be uploaded.
 */
@Getter
@Setter
@Data
public class DocumentUploadDto {
    private Long customerId;
    private String documentType;
    private long documentSize;
    private String documentName;

    // Holds the uploaded file
    private MultipartFile documentFile;
}
