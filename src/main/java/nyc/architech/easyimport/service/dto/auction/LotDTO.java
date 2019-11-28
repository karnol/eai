package nyc.architech.easyimport.service.dto.auction;

import lombok.Data;
import nyc.architech.easyimport.domain.enums.Currency;
import nyc.architech.easyimport.domain.enums.Unit;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class LotDTO {
    //TODO: move it!
    private static final String AVTO_JP_COUNTRY = "JAPAN";

    private Long id;

    private Long platformId;

    private String externalId;
    private String number;
    private String status;

    private String make;
    private String model;
    private String modification;
    private String chassis;

    private String auctionCountry = AVTO_JP_COUNTRY;
    private Instant auctionDate;
    private String auctionName;
    private Integer year;

    private Double mileage;
    private Unit mileageUnit;
    private String color;
    private String colorOriginal;
    private OptionsDTO options;

    private BigDecimal price;
    private BigDecimal finalPrice;
    private BigDecimal averagePrice;
    private List<BigDecimal> priceHistory;
    private Currency currency;

    private BigDecimal priceOriginal;
    private BigDecimal finalPriceOriginal;
    private BigDecimal averagePriceOriginal;
    private List<BigDecimal> priceOriginalHistory;
    private Currency currencyOriginal;


    private List<String> images;

    private String serialNumber;
    private String rate;
    private String interiorRate;
    private String exteriorRate;
    private String inspection;

    //TODO: these fields should be in catalog table!!! this is temporary solution!
    private Integer engineDisplacementCC;
    private String power;
    private String transmissionType;
    private String driveWheel;
}
