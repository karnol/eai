package nyc.architech.easyimport.service.task;

import nyc.architech.easyimport.EasyimportApp;
import nyc.architech.easyimport.domain.Settings;
import nyc.architech.easyimport.service.SettingsService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest(classes = EasyimportApp.class)
class UpdateCurrencyRateIT {

    @Autowired
    UpdateCurrencyRate updateCurrencyRate;
    @Autowired
    SettingsService settingsService;

    @Test
    void updateIt() {
        Instant now = Instant.now();
        updateCurrencyRate.updateIt();

        Settings usdToJpy = settingsService.getSettingsByName(Settings.USD_TO_JPY_RATE);
        System.out.println(usdToJpy);
        Assert.assertNotNull(usdToJpy);
        Assert.assertNotNull(usdToJpy.getValue());
        //TODO: fix it!!
//        Assert.assertTrue(usdToJpy.getModified().isAfter(now));
    }
}
