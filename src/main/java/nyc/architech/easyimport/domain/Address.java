package nyc.architech.easyimport.domain;

import lombok.Data;
import nyc.architech.easyimport.service.dto.AddressDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A user.
 */
@Entity
@Data
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String address;

    @Column
    private String address2;

    @Column
    private String zipCode;

    @Column
    private String state;

    @Column
    private String city;

    public static Address fromDto(AddressDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        Address address = new Address();
        address.id = dto.getId();
        address.address = dto.getAddress();
        address.address2 = dto.getAddress2();
        address.zipCode = dto.getZipCode();
        address.state = dto.getState();
        address.city = dto.getCity();
        return address;
    }
}
