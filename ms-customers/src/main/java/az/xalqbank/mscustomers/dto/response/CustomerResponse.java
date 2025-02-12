package az.xalqbank.mscustomers.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String profilePhotoUrl;
}
