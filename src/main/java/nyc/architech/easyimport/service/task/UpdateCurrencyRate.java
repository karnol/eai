package nyc.architech.easyimport.service.task;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nyc.architech.easyimport.domain.Settings;
import nyc.architech.easyimport.service.SettingsService;
import nyc.architech.easyimport.service.dto.task.OpenExchangeRatesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Objects;

import static nyc.architech.easyimport.service.dto.task.OpenExchangeRatesDTO.JPY;

/**
 * We update currency rates using 1000 free monthly requests to https://openexchangerates.org
 */
@Component
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:config/custom/task.properties")
public class UpdateCurrencyRate {

    @Value("${task.currency.rate.url}")
    private String currencyRateUrl;

    private final RestTemplate restTemplate;
    private final SettingsService settingsService;

    @PostConstruct
    public void init() {
        updateIt();
    }

    @Scheduled(fixedDelayString = "${task.currency.rate.fixedDelayString}")
    public void updateIt() {
        try {

            OpenExchangeRatesDTO dto = restTemplate.getForObject(currencyRateUrl, OpenExchangeRatesDTO.class);
            Float rateUsdToJpy = null;
            if (Objects.nonNull(dto) && Objects.nonNull(dto.getRates())) {
                rateUsdToJpy = dto.getRates().get(JPY);
            }
            if (Objects.nonNull(rateUsdToJpy)) {
                settingsService.createOrUpdate(Settings.USD_TO_JPY_RATE, rateUsdToJpy.toString());
            }
        } catch (RestClientException ex) {
            log.debug("Troubles reaching URL to update currency rates", ex);
        }
    }
}
