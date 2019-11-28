package nyc.architech.easyimport.web.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nyc.architech.easyimport.EasyimportApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AuctionResource} REST controller.
 */
@SpringBootTest(classes = EasyimportApp.class)
public class AuctionResourceIT {

    private static final String URL = "/api/auction-lots";
    private static final String TEST_MAKE_PARAM = "make";
    private static final String TEST_MAKE_VALUE = "TOYOTA";
    private static final String TEST_MODEL_PARAM = "model";
    private static final String TEST_MODEL_VALUE = "COROLLA";
    private static final String TEST_AUCTION_PARAM = "auction";
    private static final String TEST_AUCTION_VALUE = "JU Hokkaido";

    private static final String TEST_YEAR_PARAM = "year";
    private static final String TEST_YEAR_VALUE = "1999,2004";

    private static final String TEST_PRICE_USD_PARAM = "priceUSD";
    private static final String TEST_PRICE_USD_VALUE = "90,100";

    private static final String TEST_MILEAGE_MI_PARAM = "mileageMi";
    private static final String TEST_MILEAGE_MI_VALUE = "40000,50000";

    private static final String TEST_ENGINE_DISPLACEMENT_CC_PARAM = "engineDisplacementCC";
    private static final String TEST_ENGINE_DISPLACEMENT_CC_VALUE = "1490,1490";

    private static final String TEST_TRANSMISSION_PARAM = "transmissionType";
    private static final String TEST_TRANSMISSION_VALUE = "AUTOMATIC";
    private static final String TEST_TRANSMISSION_VALUE_2 = "MANUAL";

    private static final String TEST_COLOR_PARAM = "color";
    private static final String TEST_COLOR_VALUE = "silver";
    private static final String TEST_COLOR_VALUE_2 = "green";

    private static final String TEST_GIBBERISH_ID = "GIBBERRISHID";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc restMvc;

    @BeforeEach
    public void setUp() {
        this.restMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testAuctionLotsSearch() throws Exception {
        //check that we will find one Page of auctionLots for certain filters
        MvcResult result = restMvc.perform(get(URL + "?" + TEST_MAKE_PARAM + "=" + TEST_MAKE_VALUE
                                                     + "&" + TEST_MODEL_PARAM + "=" + TEST_MODEL_VALUE
//                                                     + "&" + TEST_AUCTION_PARAM + "=" + TEST_AUCTION_VALUE
                                                     + "&" + TEST_YEAR_PARAM + "=" + TEST_YEAR_VALUE
//                                                     + "&" + TEST_PRICE_USD_PARAM + "=" + TEST_PRICE_USD_VALUE
//                                                     + "&" + TEST_MILEAGE_MI_PARAM + "=" + TEST_MILEAGE_MI_VALUE
//                                                     + "&" + TEST_ENGINE_DISPLACEMENT_CC_PARAM + "=" + TEST_ENGINE_DISPLACEMENT_CC_VALUE
//                                                     + "&" + TEST_TRANSMISSION_PARAM + "=" + TEST_TRANSMISSION_VALUE
//                                                     + "&" + TEST_COLOR_PARAM + "=" + TEST_COLOR_VALUE
                                                     //+ "&" + TEST__PARAM + "=" + TEST__VALUE
                                              )
                                              .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            //by default one page has 20 items
//            .andExpect(jsonPath("$.content", hasSize(20)))
            .andExpect(jsonPath("$.content[*].make", everyItem(equalTo(TEST_MAKE_VALUE))))
            .andExpect(jsonPath("$.content[*].model", everyItem(equalTo(TEST_MODEL_VALUE))))
            .andReturn()
            ;

        //for some search result we will fetch full description
        String someExternalId = getId(result);
        restMvc.perform(get(URL + "/" + someExternalId)
                                     .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(jsonPath("$.externalId").value(someExternalId))
                    .andReturn()
        ;

        //check 404th status for non-existing ID
        restMvc.perform(get(URL + "/" + TEST_GIBBERISH_ID)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
        ;
    }

    @Test
    public void testAuctionLotsSearchForSpecialCases() throws Exception {
        restMvc.perform(get(URL + "?" + TEST_MAKE_PARAM + "=" + TEST_MAKE_VALUE)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            //by default one page has 20 items
//            .andExpect(jsonPath("$.content", hasSize(20)))
            .andExpect(jsonPath("$.content[*].make", everyItem(equalTo(TEST_MAKE_VALUE))))
            .andReturn()
            ;
        restMvc.perform(get(URL + "?" +  TEST_COLOR_PARAM + "=" + TEST_COLOR_VALUE_2)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.content[*].color", everyItem(equalTo(TEST_COLOR_VALUE_2))))
            .andReturn()
        ;

        restMvc.perform(get(URL + "?" + TEST_TRANSMISSION_PARAM + "=" + TEST_TRANSMISSION_VALUE_2)
                            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.content[*].transmissionType", everyItem(equalTo(TEST_TRANSMISSION_VALUE_2))))
            .andReturn()
        ;
    }


    private String getId(MvcResult result) throws Exception {
        String response = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        return node.get("content").get(0).get("externalId").asText();
    }
}
