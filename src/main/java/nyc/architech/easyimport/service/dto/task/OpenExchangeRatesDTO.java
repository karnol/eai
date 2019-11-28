package nyc.architech.easyimport.service.dto.task;

import lombok.Data;

import java.util.Map;

@Data
public class OpenExchangeRatesDTO {

    public static final String JPY = "JPY";

    private String disclaimer;
    private String license;
    private Long timestamp;
    private String base;
    private Map<String, Float> rates;
}
