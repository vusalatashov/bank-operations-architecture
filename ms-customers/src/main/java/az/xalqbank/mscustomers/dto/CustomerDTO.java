package az.xalqbank.mscustomers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long id;             // İsteğe bağlı; yanıt olarak gönderilebilir
    private String name;         // Müşteri adı
    private String email;        // Müşteri e-posta
    private String phoneNumber;  // Müşteri telefon numarası
    private ProfilePhotoDTO profilePhoto;  // İlişkili profil fotoğrafı bilgileri
}
