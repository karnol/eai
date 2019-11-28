package nyc.architech.easyimport.service.avtojp;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import nyc.architech.easyimport.domain.Settings;
import nyc.architech.easyimport.service.dto.auction.AuctionSearchDTO;
import nyc.architech.easyimport.service.util.AvtoJpUtil;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Getter
public enum AvtoJpParam {
    EXTERNAL_ID("id", dto -> null)
    ,AUCTION("auction", AuctionSearchDTO::getAuction)
    ,MAKE("marka_name", AuctionSearchDTO::getMake)
    ,MODEL("model_name", AuctionSearchDTO::getModel)
    ,YEAR("year", AuctionSearchDTO::getYear, true)
    ,PRICE_USD("start", AuctionSearchDTO::getPriceUSD, true, Settings.USD_TO_JPY_RATE)
    ,MILEAGE_MI("mileage", AuctionSearchDTO::getMileageMi, true, AvtoJpUtil.MI_TO_KM, false, null, null)
    ,ENGINE_DISPLACEMENT_CC("eng_v", AuctionSearchDTO::getEngineDisplacementCC, true)
    ,TRANSMISSION_TYPE("kpp_type", AuctionSearchDTO::getTransmissionType, ImmutableMap.of("MANUAL", " in (0, 1)",
                                                   "AUTOMATIC", " = 2"))
    ,COLOR("color", AuctionSearchDTO::getColor, false, null, true, null, null);

    private String columnName;
    private Function<AuctionSearchDTO, String> valueGetter;
    private boolean isNumeric;
    private Float koef;
    private boolean like;
    private String settingsKoefName;
    private Map<String, String> dictionary;

    public boolean isDictionary() {
        return Objects.nonNull(this.dictionary);
    }

    public String getDictionaryCondition(String key) {
        return dictionary.get(key);
    }

    AvtoJpParam(String columnName, Function<AuctionSearchDTO, String> valueGetter) {
        this(columnName, valueGetter, false);
    }

    AvtoJpParam(String columnName, Function<AuctionSearchDTO, String> valueGetter, boolean isNumeric) {
        this(columnName, valueGetter, isNumeric, null, false, null, null);
    }

    AvtoJpParam(String columnName, Function<AuctionSearchDTO, String> valueGetter, boolean isNumeric, String settingsKoefName) {
        this(columnName, valueGetter, isNumeric, null, false, settingsKoefName, null);
    }

    AvtoJpParam(String columnName, Function<AuctionSearchDTO, String> valueGetter, Map<String, String> dictionary) {
        this(columnName, valueGetter, false, null, false, null, dictionary);
    }

    AvtoJpParam(String columnName, Function<AuctionSearchDTO, String> valueGetter, boolean isNumeric, Float koef, boolean like,
                String settingsKoefName, Map<String, String> dictionary) {
        this.columnName = columnName;
        this.valueGetter = valueGetter;
        this.isNumeric = isNumeric;
        this.koef = koef;
        this.like = like;
        this.settingsKoefName = settingsKoefName;
        this.dictionary = dictionary;
    }
}
