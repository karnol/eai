package nyc.architech.easyimport.domain;

import lombok.Data;
import nyc.architech.easyimport.service.dto.auction.LotDTO;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.Instant;

@Entity
@Data
public class WatchLot extends AbstractLot {

    public static final String PRICES_HISTORY_LIST_SEPARATOR = ";";
    public static final String PRICE_SEPARATOR = "%";

    //previous prices (in USD) history in format - priceUSD1%DateOfPrice1;priceUSD2%DateOfPrice2
    //where DateOfPrice is corresponding Instant.getEpochSecond()
    @Column
    private String previousPricesHistory;
    @Column
    protected String status;

    public static WatchLot fromAuctionLotDTO(LotDTO lotDTO) {
        WatchLot watchLot = new WatchLot();

//        watchLot.platformId = Platform.AVTO_JP_ID;
        watchLot.externalId = lotDTO.getExternalId();
        watchLot.lotNumber = lotDTO.getNumber();

        watchLot.make = lotDTO.getMake();
        watchLot.model = lotDTO.getModel();
        watchLot.modification = lotDTO.getModification();
        watchLot.chassis = lotDTO.getChassis();

        watchLot.auctionDate = lotDTO.getAuctionDate();
        watchLot.auction = lotDTO.getAuctionName();
        watchLot.year = lotDTO.getYear().shortValue();
        watchLot.averagePriceUSD = lotDTO.getAveragePrice().intValue();
        watchLot.mileageMi = Long.valueOf(Math.round(lotDTO.getMileage())).intValue();
        watchLot.color = lotDTO.getColor();
        watchLot.options = lotDTO.getOptions().toDbString();

        watchLot.priceUSD = lotDTO.getPrice().intValue();
        watchLot.priceOriginal = lotDTO.getPrice().intValue();
        watchLot.previousPricesHistory = "";
        watchLot.status = lotDTO.getStatus();

        watchLot.rate = lotDTO.getRate();
        watchLot.rateInt = lotDTO.getInteriorRate();
        watchLot.rateExt = lotDTO.getExteriorRate();
        watchLot.inspection = lotDTO.getInspection();
        watchLot.images = CollectionUtils.isEmpty(lotDTO.getImages())
                                ? "" :  String.join(IMAGES_DELIMITER, lotDTO.getImages());

        return watchLot;
    }
}
