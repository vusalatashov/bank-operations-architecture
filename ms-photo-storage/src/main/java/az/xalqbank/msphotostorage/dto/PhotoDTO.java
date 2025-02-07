package az.xalqbank.msphotostorage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Fotoğrafın dışa açık veri transfer objesi (DTO)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDTO {

    private Long id;             // Photo tablosundaki ID
    private String fileName;     // Orijinal dosya adı
    private String fileUrl;      // Dosyaya erişim URL'si
    private Long customerId;     // Bu fotoğrafın ait olduğu müşteri ID'si
}