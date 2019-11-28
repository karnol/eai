package nyc.architech.easyimport.service.dto;

import lombok.Data;
import nyc.architech.easyimport.domain.Address;
import nyc.architech.easyimport.domain.Authority;
import nyc.architech.easyimport.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
@Data
public class UserDTO {

    private Long id;

//    @NotBlank
//    @Pattern(regexp = Constants.LOGIN_REGEX)
//    @Size(min = 1, max = 50)
//    private String login;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    private String phone;

    private AddressDTO shippingAddress;

    private AddressDTO mailingAddress;

    private AddressDTO billingAddress;
//    @Size(max = 256)
//    private String imageUrl;

    private Boolean newsSubscription;

    private Boolean offerSubscription;

    private boolean activated;

    @Size(min = 2, max = 6)
    private String langKey;

//    private String createdBy;
//
//    private Instant createdDate;
//
//    private String lastModifiedBy;
//
//    private Instant lastModifiedDate;

    private Set<String> authorities;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this.id = user.getId();
//        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.newsSubscription = user.getNewsSubscription();
        this.offerSubscription = user.getOfferSubscription();
        this.activated = user.getActivated();

        this.shippingAddress = addressToAddressDTO(user.getShippingAddress());
        this.mailingAddress = addressToAddressDTO(user.getMailingAddress());
        this.billingAddress = addressToAddressDTO(user.getBillingAddress());
//        this.imageUrl = user.getImageUrl();
        this.langKey = user.getLangKey();
//        this.createdBy = user.getCreatedBy();
//        this.createdDate = user.getCreatedDate();
//        this.lastModifiedBy = user.getLastModifiedBy();
//        this.lastModifiedDate = user.getLastModifiedDate();
        this.authorities = user.getAuthorities().stream()
            .map(Authority::getName)
            .collect(Collectors.toSet());
    }

    //TODO: move it to UserMapper
    private AddressDTO addressToAddressDTO(Address address) {
        return Objects.isNull(address) ? null : AddressDTO.builder().id(address.getId()).address(address.getAddress()).address2(address.getAddress2())
            .city(address.getCity()).state(address.getState()).zipCode(address.getZipCode()).build();
    }
}
