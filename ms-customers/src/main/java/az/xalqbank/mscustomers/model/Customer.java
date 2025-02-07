package az.xalqbank.mscustomers.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Müşteri ID'si

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;  // Müşteri adı

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;  // Müşteri e-posta adresi

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    private String phoneNumber;  // Müşteri telefon numarası

    // One-to-one ilişki
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private ProfilePhoto profilePhoto;

    // Profil fotoğrafı ilişkilendirme metodu
    public void setProfilePhoto(ProfilePhoto profilePhoto) {
        if (profilePhoto != null) {
            this.profilePhoto = profilePhoto;
            profilePhoto.setCustomer(this);
        }
    }
}
