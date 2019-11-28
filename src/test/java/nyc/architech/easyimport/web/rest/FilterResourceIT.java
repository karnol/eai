package nyc.architech.easyimport.web.rest;

import nyc.architech.easyimport.EasyimportApp;
import nyc.architech.easyimport.domain.enums.FilterType;
import nyc.architech.easyimport.domain.enums.Platform;
import nyc.architech.easyimport.service.task.SyncAvtoJpTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {EasyimportApp.class})
@Transactional
public class FilterResourceIT {

    private static final String URL = "/api/filter";
    private static final String URL_AVTO_JP = "/api/filter/" + Platform.AVTO_JP.name();
    private static final String URL_PLATFORM = "/api/platform";
    private static final String PARAM_PLATFORM = "platform_id";
    private static final String PARAM_PLATFORM_VALUE = Platform.AVTO_JP.name();
    private static final String PARAM_PARENT = "parent";
    private static final String PARAM_PARENT_VALUE = "1";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private SyncAvtoJpTask syncAvtoJpTask;

    private MockMvc restMvc;

    @BeforeEach
    public void setUp() {
        this.restMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetPlatform() throws Exception {
        restMvc.perform(get(URL_PLATFORM)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andReturn();
    }

    @Test
    public void testGetFiltersByType() throws Exception {

        syncAvtoJpTask.syncAllFilters();

        //check that we will find filters for all filterTypes
        restMvc.perform(get(URL_AVTO_JP + "/" + FilterType.MAKE)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andReturn();

        restMvc.perform(get(URL_AVTO_JP + "/" + FilterType.AUCTION)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andReturn();

        //here is tricky one - with parentExternalId!
        restMvc.perform(get(URL_AVTO_JP + "/" + FilterType.MODEL + "?" + PARAM_PARENT + "=" + PARAM_PARENT_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andReturn();

        restMvc.perform(get(URL_AVTO_JP + "/" + FilterType.TRANSMISSION_TYPE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andReturn();

        restMvc.perform(get(URL_AVTO_JP + "/" + FilterType.COLOR))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andReturn();

        //non-existing platform
        restMvc.perform(get(URL + "/NON_EXISTING_PLATFORM" + "/" + FilterType.COLOR)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn();
        //non-existing filter type
        restMvc.perform(get(URL_AVTO_JP + "/" + "NON_EXISTING")
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn();
    }
}
