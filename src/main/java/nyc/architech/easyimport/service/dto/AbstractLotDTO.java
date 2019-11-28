package nyc.architech.easyimport.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class AbstractLotDTO {

    protected Long id;

    protected Long platformId;
    protected String externalId;
    protected String lotNumber;

    protected String make;
    protected String model;
    protected String modification;
    protected String chassis;

    protected Instant auctionDate;
    protected String auction;
    protected Short year;
    protected Integer averagePriceUSD;
    protected Integer mileageMi;
    protected String color;
    protected String options;

    protected Integer priceUSD;
    protected Integer priceOriginal;

    protected String rate;
    protected String rateInt;
    protected String rateExt;
    protected String inspection;
    protected List<String> images;
}
