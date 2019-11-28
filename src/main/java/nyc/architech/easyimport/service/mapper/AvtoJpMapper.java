package nyc.architech.easyimport.service.mapper;

import nyc.architech.easyimport.domain.enums.Currency;
import nyc.architech.easyimport.domain.enums.Unit;
import nyc.architech.easyimport.service.dto.auction.AvtoJpLotDTO;
import nyc.architech.easyimport.service.dto.auction.LotDTO;
import nyc.architech.easyimport.service.dto.auction.OptionsDTO;
import nyc.architech.easyimport.service.util.AvtoJpUtil;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;

import static nyc.architech.easyimport.service.util.AvtoJpUtil.*;

@Mapper(componentModel = "spring")
public interface AvtoJpMapper {

    @Mapping(source = "options", target = "options", qualifiedByName="options")
    @Mapping(source = "imagesString", target = "images", qualifiedByName="images")
    @Mapping(source = "transmissionType", target = "transmissionType", qualifiedByName="transmissionType")
    @Mapping(source = "driveWheel", target = "driveWheel", qualifiedByName="driveWheel")
    @Mapping(source = "color", target = "color", qualifiedByName="color")
    @Mapping(source = "color", target = "colorOriginal")
    @Mapping(source = "rate", target = "rate", qualifiedByName="rate")
    @Mapping(source = "mileageKm", target = "mileage", qualifiedByName="mileage")
    @Mapping(source = "priceOriginalHistoryAsString", target = "priceOriginalHistory", qualifiedByName="priceHistory")
    LotDTO avtoJpLotDTOToLotDTO(AvtoJpLotDTO avtoJpLotDTO);

    List<LotDTO> avtoJpLotDTOListToLotDTOList(List<AvtoJpLotDTO> avtoJpLotDTOList);

    @Named("options")
    default OptionsDTO optionsToOptionsDTO(String options) {
        return OptionsDTO.parseFromEquip(StringUtils.stripToEmpty(options));
    }

    @Named("images")
    default List<String> imagesStringToImages(String imagesString) {
        return adjustImages(imagesString);
    }

    @Named("transmissionType")
    default String transmissionTypeToTransmissionType(Byte transmissionType) {
        return adjustTransmissionType(transmissionType);
    }

    @Named("driveWheel")
    default String driveWheelToDriveWheel(String driveWheel) {
        return adjustDriveWheel(driveWheel);
    }

    @Named("color")
    default String colorToColor(String color) {
        return adjustColor(color);
    }

    @Named("rate")
    default String rateToRate(String rate) {
        return adjustRate(rate);
    }

    @Named("mileage")
    default Double mileageKmToMileage(Integer mileageKm) {
        return adjustMileage(mileageKm);
    }

    @Named("priceHistory")
    default List<BigDecimal> priceHistoryAsStringToPriceOriginalHistory(String priceHistoryAsString) {
        return AvtoJpUtil.splitPriceHistory(priceHistoryAsString);
    }

    @AfterMapping
    default void adjustLotDTO(AvtoJpLotDTO avtoJpLotDTO, @MappingTarget LotDTO lotDTO) {
        lotDTO.setCurrency(Currency.USD);
        lotDTO.setCurrencyOriginal(Currency.JPY);
        lotDTO.setMileageUnit(Unit.MI);

        AvtoJpUtil.addExtraInfo(lotDTO, avtoJpLotDTO.getInfo());
    }
}
