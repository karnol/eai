package nyc.architech.easyimport.service.dto.auction;

import lombok.Data;
import nyc.architech.easyimport.domain.enums.FilterType;
import nyc.architech.easyimport.domain.enums.Platform;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class FilterDTO {

    private Long id;
    private String externalId;
    private String name;
    private String parentExternalId;
    private String type;
    private String platform;
    private String additionalInfo;
}
