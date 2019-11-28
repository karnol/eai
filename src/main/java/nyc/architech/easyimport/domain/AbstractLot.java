package nyc.architech.easyimport.domain;

import lombok.Data;
import nyc.architech.easyimport.service.dto.auction.LotDTO;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@MappedSuperclass
@Data
public class AbstractLot {

    public static final String IMAGES_DELIMITER = "%%";

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected Long platformId;

    @Column
    protected Long userId;

    @Column
    protected String externalId;
    @Column
    protected String lotNumber;

    @Column
    protected String make;
    @Column
    protected String model;
    @Column
    protected String modification;
    @Column
    protected String chassis;

    @Column
    protected Instant auctionDate;
    @Column
    protected String auction;
    @Column
    protected Short year;
    @Column(name = "average_price_usd")
    protected Integer averagePriceUSD;
    @Column
    protected Integer mileageMi;
    @Column
    protected String color;
    @Column
    protected String options;
    @Column
    protected String rate;
    @Column
    protected String rateInt;
    @Column
    protected String rateExt;
    @Column
    protected String inspection;
    //store all url of images as one big String
    @Column
    protected String images;

    @Column(name = "price_usd")
    protected Integer priceUSD;
    @Column
    protected Integer priceOriginal;
}
