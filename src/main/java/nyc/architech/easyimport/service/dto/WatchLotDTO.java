package nyc.architech.easyimport.service.dto;

import lombok.Data;
import nyc.architech.easyimport.domain.AbstractLot;
import nyc.architech.easyimport.domain.WatchLot;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nyc.architech.easyimport.domain.WatchLot.PRICES_HISTORY_LIST_SEPARATOR;

@Data
public class WatchLotDTO extends AbstractLotDTO {

    private List<PriceDTO> previousPricesHistory;
    private String status;

    public static WatchLotDTO fromEntity(WatchLot entity) {
        WatchLotDTO dto = new WatchLotDTO();
        dto.id = entity.getId();

        dto.platformId = entity.getPlatformId();
        dto.externalId = entity.getExternalId();
        dto.lotNumber = entity.getLotNumber();

        dto.make = entity.getMake();
        dto.model = entity.getModel();
        dto.modification = entity.getModification();
        dto.chassis = entity.getChassis();

        dto.auctionDate = entity.getAuctionDate();
        dto.auction = entity.getAuction();
        dto.year = entity.getYear().shortValue();
        dto.averagePriceUSD = entity.getAveragePriceUSD().intValue();
        dto.mileageMi = Long.valueOf(Math.round(entity.getMileageMi())).intValue();
        dto.color = entity.getColor();
        dto.options = entity.getOptions();

        dto.rate = entity.getRate();
        dto.rateInt = entity.getRateInt();
        dto.rateExt = entity.getRateExt();
        dto.inspection = entity.getInspection();
        dto.images = StringUtils.isBlank(entity.getImages()) ? Lists.newArrayList() :
                            Arrays.asList(entity.getImages().split(AbstractLot.IMAGES_DELIMITER));

        dto.priceUSD = entity.getPriceUSD();
        dto.priceOriginal = entity.getPriceOriginal();
//        dto.previousPricesHistory = Stream.of(entity.getPreviousPricesHistory()
//                                                .split(PRICES_HISTORY_LIST_SEPARATOR))
//                                        .filter(StringUtils::isNoneEmpty)
//                                        .map(PriceDTO::of)
//                                        .collect(Collectors.toList());
        dto.status = entity.getStatus();

        return dto;
    }
}
