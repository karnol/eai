package nyc.architech.easyimport.service.dto.auction;

import lombok.Data;

@Data
public class OptionsDTO {
    private static final String POWER_STEERING = "PS";
    private static final String POWER_WINDOWS = "PW";
    private static final String ALLOY_WHEELS = "AW";
    private static final String REAR_SPOILER = "RS";
    private static final String HATCH = "SR";
    private static final String AIRBAG = "AB";
    private static final String AIR_CONDITIONING = "AC";
    private static final String CLIMATE_CONTROL = "AAC";
    private Boolean hasPowerSteering;
    private Boolean hasPowerWindows;
    private Boolean hasAlloyWheels;
    private Boolean hasRearSpoiler;
    private Boolean hasHatch;
    private Boolean hasAirbag;
    private Boolean hasAirConditioning;
    private Boolean hasClimateControl;

    public static OptionsDTO parseFromEquip(String equip) {
        OptionsDTO dto = new OptionsDTO();
        dto.hasPowerSteering = extractBool(equip, POWER_STEERING);
        dto.hasPowerWindows = extractBool(equip, POWER_WINDOWS);
        dto.hasAlloyWheels = extractBool(equip, ALLOY_WHEELS);
        dto.hasRearSpoiler = extractBool(equip, REAR_SPOILER);
        dto.hasHatch = extractBool(equip, HATCH);
        dto.hasAirbag = extractBool(equip, AIRBAG);
        dto.hasClimateControl = extractBool(equip, CLIMATE_CONTROL);
        dto.hasAirConditioning = dto.hasClimateControl ? false : extractBool(equip, AIR_CONDITIONING);
        return dto;
    }

    private static Boolean extractBool(String whole, String part) {
        return whole.contains(part);
    }

    public String toDbString() {
        return
            (this.hasPowerSteering ? POWER_STEERING + " " : "") +
            (this.hasPowerWindows ? POWER_WINDOWS + " " : "") +
            (this.hasAlloyWheels ? ALLOY_WHEELS + " " : "") +
            (this.hasRearSpoiler ? REAR_SPOILER + " " : "") +
            (this.hasHatch ? HATCH + " " : "") +
            (this.hasAirbag ? AIRBAG + " " : "") +
            (this.hasAirConditioning ? AIR_CONDITIONING + " " : "") +
            (this.hasClimateControl ? CLIMATE_CONTROL + " ": "")
            ;
    }
}
