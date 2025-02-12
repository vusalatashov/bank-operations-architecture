package az.xalqbank.msfileupload.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity representing a photo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private String fileName;
    private String fileUrl;
}
