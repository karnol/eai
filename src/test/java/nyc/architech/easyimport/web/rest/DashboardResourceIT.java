package nyc.architech.easyimport.web.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nyc.architech.easyimport.EasyimportApp;
import nyc.architech.easyimport.domain.User;
import nyc.architech.easyimport.repository.UserRepository;
import nyc.architech.easyimport.service.avtojp.impl.AvtoJpAuctionService;
import nyc.architech.easyimport.service.dto.auction.LotDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AuctionResource} REST controller.
 */
@SpringBootTest(classes = EasyimportApp.class)
public class DashboardResourceIT {

    private static final String URL = "/api/dashboard/watch";

    @Autowired
    private WebApplicationContext webApplicationContext;
    //TODO: we need AuctionService here!
    @Autowired
    private AvtoJpAuctionService auctionLotService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc restMvc;

    @BeforeEach
    public void setUp() {
        this.restMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Transactional
    @WithMockUser("test-watch-account@example.com")
    @Disabled
    public void testWatchCrud() throws Exception {
        User user = new User();
        user.setLogin("test-watch-account@example.com");
        user.setEmail("test-watch-account@example.com");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);

        userRepository.saveAndFlush(user);

        restMvc.perform(get(URL)
                        .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", empty()))
            .andReturn()
            ;

        List<LotDTO> auctionLots = auctionLotService.find(3);

        restMvc.perform(post(URL).param("external_id", auctionLots.get(0).getExternalId())
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            ;
        //try to add the same lot twice
        restMvc.perform(post(URL).param("external_id", auctionLots.get(0).getExternalId())
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
        ;
        restMvc.perform(post(URL).param("external_id", auctionLots.get(1).getExternalId())
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
        ;
        restMvc.perform(post(URL).param("external_id", auctionLots.get(2).getExternalId())
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
        ;

        MvcResult result = restMvc.perform(get(URL)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", hasSize(3)))
            .andReturn();

        restMvc.perform(delete(URL + "/" + getId(result))
                            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        result = restMvc.perform(get(URL)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", hasSize(2)))
            .andReturn();

        //now we would check how SyncWatchList.checkIt will synchronize our WatchLots
        //TODO
//        Long watchLotId = getId(result);
//        WatchLot watchLot = watchLotRepository.getOne(watchLotId);
//        watchLot.setPriceOriginal(77777777);
//        watchLotRepository.save(watchLot);
//        syncWatchList.checkIt();

        restMvc.perform(get(URL)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$", hasSize(2)))
            //TODO
//            .andExpect(jsonPath(String.format("$[?(@.id != %d)].previousPricesHistory[*]", watchLotId), empty()))
//            .andExpect(jsonPath(String.format("$[?(@.id == %d)].previousPricesHistory", watchLotId), hasSize(1)))
//            .andExpect(jsonPath(String.format("$[?(@.id == %d)].previousPricesHistory[*].price", watchLotId), hasSize(1)))
            .andReturn();
    }

    @Test
    public void testNotAuthorizedGetWatchList() throws Exception {
        restMvc.perform(get(URL)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andReturn()
        ;
    }

    private Long getId(MvcResult result) throws Exception {
        String response = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        return Long.parseLong(node.get(0).get("id").asText());
    }
}
