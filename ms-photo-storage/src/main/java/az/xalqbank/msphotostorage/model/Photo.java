package az.xalqbank.msphotostorage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Bu fotoğrafın hangi müşteriye ait olduğunu belirtmek için
    private Long customerId;

    // Dosyanın orijinal adı
    private String fileName;

    // Dosyanın saklandığı konumun URL'si
    private String fileUrl;
}