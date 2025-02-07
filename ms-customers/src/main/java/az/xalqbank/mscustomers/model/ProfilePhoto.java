package az.xalqbank.mscustomers.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profile_photos")
public class ProfilePhoto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "File name is required")
    @Size(min = 3, max = 255, message = "File name must be between 3 and 255 characters")
    private String fileName;

    @NotBlank(message = "File URL is required")
    private String fileUrl;  // Yükleme sonrası oluşturulan dosya URL'si

    @NotNull(message = "File size is required")
    private Long size;  // Dosya boyutu (byte cinsinden)

    @NotBlank(message = "File format is required")
    @Size(min = 3, max = 10, message = "Format must be between 3 and 10 characters")
    private String format;  // Dosya formatı (jpg, png vs.)

    @OneToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null && customer.getProfilePhoto() != this) {
            customer.setProfilePhoto(this);
        }
    }
}
