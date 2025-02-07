package az.xalqbank.mscustomers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePhotoDTO {
    private Long id;         // İsteğe bağlı; yanıt olarak gönderilebilir
    private String fileName; // Dosya adı
    private String fileUrl;  // Yükleme sonrası dosya URL'si
    private Long size;       // Dosya boyutu
    private String format;   // Dosya formatı (jpg, png, vs.)
}
