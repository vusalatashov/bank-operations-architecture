package az.xalqbank.msphotostorage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoMetadata {

    @Id
    private String id;

    private String name;
    private String contactDetails;
    private String fileName;
    private String fileUrl;
    private long fileSize;
    private String fileType;
    private LocalDateTime uploadTime;
}
