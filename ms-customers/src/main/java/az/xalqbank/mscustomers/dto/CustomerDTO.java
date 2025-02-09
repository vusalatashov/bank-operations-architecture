package az.xalqbank.mscustomers.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)  // Ignore unknown fields like "id"
public class CustomerDTO {
    private String name;
    private String email;
    private String phoneNumber;
}
