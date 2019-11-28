package nyc.architech.easyimport.service.dto;

import lombok.Data;
import nyc.architech.easyimport.domain.enums.Currency;

import java.math.BigDecimal;

@Data
public class PriceDTO {

    BigDecimal price;
    Currency currency;
}
