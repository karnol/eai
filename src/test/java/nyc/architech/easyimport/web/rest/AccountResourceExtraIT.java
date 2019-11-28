package nyc.architech.easyimport.web.rest;

import nyc.architech.easyimport.EasyimportApp;
import nyc.architech.easyimport.domain.User;
import nyc.architech.easyimport.repository.AddressRepository;
import nyc.architech.easyimport.repository.UserRepository;
import nyc.architech.easyimport.service.dto.AddressDTO;
import nyc.architech.easyimport.web.rest.vm.LoginVM;
import nyc.architech.easyimport.web.rest.vm.ManagedUserVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AccountResource} REST controller.
 */
@SpringBootTest(classes = EasyimportApp.class)
public class AccountResourceExtraIT {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc restMvc;

    @BeforeEach
    public void setup() {
        this.restMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Transactional
    public void testRegisterValid() throws Exception {

        int addressCount = addressRepository.findAll().size();

        ManagedUserVM userDto = new ManagedUserVM();
        userDto.setPassword("password");
        userDto.setFirstName("Alice");
        userDto.setLastName("Test");
        userDto.setEmail("test-register-valid@example.com");

        AddressDTO shippingAddr = AddressDTO.builder().address("Shipping addr1").address2("Shipping addr2")
            .zipCode("000001").state("Shipping State").city("Shipping City").build();

        AddressDTO mailingAddr = AddressDTO.builder().address("Mailing addr1").address2("Mailing addr2")
            .zipCode("000002").state("Mailing State").city("Mailing City").build();

        userDto.setShippingAddress(shippingAddr);
        userDto.setMailingAddress(mailingAddr);

        assertFalse(userRepository
                        .findOneByLogin("test-register-valid")
                        .isPresent());

        restMvc.perform(post("/api/register")
                            .contentType(TestUtil.APPLICATION_JSON_UTF8)
                            .content(TestUtil.convertObjectToJsonBytes(userDto)))
                        .andExpect(status().isCreated());

        Optional<User> dbUser = userRepository.findOneByEmailIgnoreCase("test-register-valid@example.com");
        assertThat(dbUser.isPresent()).isTrue();
        assertThat(dbUser.get().getActivated()).isTrue();
        assertThat(dbUser.get().getShippingAddress()).isNotNull();
        assertThat(dbUser.get().getShippingAddress().getAddress()).isEqualTo("Shipping addr1");

        LoginVM login = new LoginVM();
        login.setUsername(userDto.getEmail());
        login.setPassword(userDto.getPassword());

        restMvc.perform(post("/api/authenticate")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(login)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id_token").isString())
            .andExpect(jsonPath("$.id_token").isNotEmpty())
            .andExpect(header().string("Authorization", not(nullValue())))
            .andExpect(header().string("Authorization", not(isEmptyString())));

        restMvc.perform(get("/api/account")
                            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.firstName").value("Alice"))
            .andExpect(jsonPath("$.lastName").value("Test"))
            .andExpect(jsonPath("$.shippingAddress.address").value("Shipping addr1"))
            .andExpect(jsonPath("$.email").value("test-register-valid@example.com"))
            ;

        //let's update our account
        userDto.setId(dbUser.get().getId());
        userDto.setFirstName("Olivia");
        userDto.setPhone("newphone");
        userDto.setMailingAddress(null);
        userDto.getShippingAddress().setId(dbUser.get().getShippingAddress().getId());
        userDto.getShippingAddress().setAddress("New Addr");

        AddressDTO billingAddr = AddressDTO.builder().address("Billing addr1").address2("Billing addr2")
            .zipCode("000003").state("Billing State").city("Billing City").build();

        userDto.setBillingAddress(billingAddr);

        restMvc.perform(
            post("/api/account")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userDto)))
            .andExpect(status().isOk());

        Optional<User> changedDbUser = userRepository.findOneWithAuthoritiesByLogin("test-register-valid@example.com");
        assertThat(changedDbUser.isPresent()).isTrue();
        assertThat(changedDbUser.get().getFirstName()).isEqualTo("Olivia");
        assertThat(changedDbUser.get().getId()).isEqualTo(dbUser.get().getId());
        assertThat(changedDbUser.get().getPhone()).isEqualTo("newphone");
        assertThat(changedDbUser.get().getShippingAddress().getId()).isEqualTo(dbUser.get().getShippingAddress().getId());
        assertThat(changedDbUser.get().getShippingAddress().getAddress()).isEqualTo("New Addr");
        assertThat(changedDbUser.get().getBillingAddress().getAddress()).isEqualTo("Billing addr1");
        assertThat(changedDbUser.get().getMailingAddress()).isNull();

        restMvc.perform(get("/api/account")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.firstName").value("Olivia"))
            .andExpect(jsonPath("$.id").value(userDto.getId()))
            .andExpect(jsonPath("$.mailingAddress", nullValue()))
            .andExpect(jsonPath("$.shippingAddress.id").value(dbUser.get().getShippingAddress().getId()))
            .andExpect(jsonPath("$.shippingAddress.address").value("New Addr"))
            .andExpect(jsonPath("$.billingAddress.address").value("Billing addr1"))
        ;

    }
}
