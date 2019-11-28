package nyc.architech.easyimport.service.dto;

import lombok.*;
import nyc.architech.easyimport.domain.Address;

import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private Long id;
    private String address;
    private String address2;
    private String zipCode;
    private String state;
    private String city;
}
