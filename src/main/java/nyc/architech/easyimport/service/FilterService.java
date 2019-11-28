package nyc.architech.easyimport.service;

import nyc.architech.easyimport.domain.enums.FilterType;
import nyc.architech.easyimport.domain.enums.Platform;
import nyc.architech.easyimport.service.dto.auction.FilterDTO;

import java.util.List;

public interface FilterService {

    List<FilterDTO> findByPlatformAndTypeAndParentValue(Platform platform, FilterType filterType, String parentExternalId);
}
