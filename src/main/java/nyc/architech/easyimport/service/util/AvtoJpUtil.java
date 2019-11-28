package nyc.architech.easyimport.service.util;

import nyc.architech.easyimport.service.dto.auction.LotDTO;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class AvtoJpUtil {

    public static final Float CUIN_TO_CC = 16.3871F;
    public static final Float MI_TO_KM = 1.609F;

    static final String IMAGE_DELIMITER = "#";

    private static final String DEFAULT_COLOR = "other";
    private static final String DEFAULT_RATE = "-";

    private static final String INFO_INSPECTION_KEY = "Inspection";
    private static final String INFO_RATE_EXT_KEY = "Rate ext";
    private static final String INFO_RATE_INT_KEY = "Rate int";

    private static Map<String, String> avtoJpColors = new HashMap();
    private static Map<List<String>, String> avtoJpRates = new HashMap();
    private static Map<String, String> avtoJpExtraRates = new HashMap();
    private static Map<String, String> avtoJpDriveWheels = new HashMap();

    private AvtoJpUtil() {
    }

    static {
        avtoJpColors.put("orange", "orange");
        avtoJpColors.put("yellow", "yellow");
        avtoJpColors.put("green", "green");
        avtoJpColors.put("pink", "pink");
        avtoJpColors.put("brown", "brown");
        avtoJpColors.put("beige", "beige");
        avtoJpColors.put("purple", "purple");
        avtoJpColors.put("red", "red");
        avtoJpColors.put("blue", "blue");
        avtoJpColors.put("grey", "gray");
        avtoJpColors.put("gray", "gray");
        avtoJpColors.put("black", "black");
        avtoJpColors.put("bronze", "bronze");
        avtoJpColors.put("silver", "silver");
        avtoJpColors.put("white", "white");

        avtoJpRates.put(Arrays.asList("0", "R", "RA", "A", "0.1", "0.2", "0A", "***"), "0");
        avtoJpRates.put(Arrays.asList("1"), "1");
        avtoJpRates.put(Arrays.asList("2"), "2");
        avtoJpRates.put(Arrays.asList("3"), "3");
        avtoJpRates.put(Arrays.asList("3.5"), "3.5");
        avtoJpRates.put(Arrays.asList("4"), "4");
        avtoJpRates.put(Arrays.asList("4.5"), "4.5");
        avtoJpRates.put(Arrays.asList("5"), "5");
        avtoJpRates.put(Arrays.asList("6","S"), "6");

        avtoJpExtraRates.put("A", "5");
        avtoJpExtraRates.put("B", "4");
        avtoJpExtraRates.put("C", "3");
        avtoJpExtraRates.put("D", "2");
        avtoJpExtraRates.put("E", "1");

        avtoJpDriveWheels.put("FF", "front-engine, front-wheel-drive");
        avtoJpDriveWheels.put("FULLTIME4WD", "full-time 4-wheel-drive");
        avtoJpDriveWheels.put("FR", "front-engine, rear-wheel-drive");
        avtoJpDriveWheels.put("PARTTIME4WD", "part-time 4-wheel-drive");
        avtoJpDriveWheels.put("MIDSHIP", "mid-engine layout");
        avtoJpDriveWheels.put("4WD", "4-wheel-drive");
        avtoJpDriveWheels.put("RR", "rear-engine, rear-wheel-drive");
    }

    public static List<String> adjustImages(String imagesString) {
        return Arrays.asList(StringUtils.stripToEmpty(imagesString).split(IMAGE_DELIMITER));
    }

    public static String adjustTransmissionType(Byte transmissionType) {
        return (Objects.isNull(transmissionType) ||
                    transmissionType == 0 || transmissionType == 1) ? "MANUAL" : "AUTOMATIC";
    }

    public static String adjustDriveWheel(String driveWheel) {
        return avtoJpDriveWheels.computeIfAbsent(driveWheel, Function.identity());
    }

    public static String adjustColor(String colorOriginal) {
        return avtoJpColors
            .entrySet()
            .stream()
            .filter(entry -> StringUtils.stripToEmpty(colorOriginal).toLowerCase().contains(entry.getKey()))
            .findFirst()
            .map(Map.Entry::getValue)
            .orElse(DEFAULT_COLOR);
    }

    public static String adjustRate(String rateOriginal) {
        return avtoJpRates
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().contains(StringUtils.stripToEmpty(rateOriginal).toUpperCase()))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(DEFAULT_RATE);
    }

    public static Double adjustMileage(Integer mileage) {
        return Objects.isNull(mileage) ? 0 : Double.valueOf(mileage / MI_TO_KM);
    }

    public static List<BigDecimal> splitPriceHistory(String priceHistoryAsString) {
        return Stream.of(StringUtils.stripToEmpty(priceHistoryAsString).split(","))
                    .filter(StringUtils::isNotBlank)
                    .map(Double::parseDouble)
                    .map(BigDecimal::valueOf)
                    .map(bd -> bd.multiply(BigDecimal.valueOf(1000l)))
                    .collect(Collectors.toList());
    }

    public static void addExtraInfo(LotDTO lotDTO, String info) {
            Map<String, String> infoMap =
                Stream.of(StringUtils.stripToEmpty(info).split(","))
                    .filter(StringUtils::isNotBlank)
                    .map(str -> str.contains(":") ? new AbstractMap.SimpleEntry<>(str.split(":")[0].trim(), str.split(":")[1].trim()) : null)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            lotDTO.setInspection(infoMap.get(INFO_INSPECTION_KEY));
            lotDTO.setExteriorRate(infoMap.computeIfPresent(INFO_RATE_EXT_KEY, (k, v) -> avtoJpExtraRates.getOrDefault(v, DEFAULT_RATE)));
            lotDTO.setInteriorRate(infoMap.computeIfPresent(INFO_RATE_INT_KEY, (k, v) -> avtoJpExtraRates.getOrDefault(v, DEFAULT_RATE)));
    }
}
