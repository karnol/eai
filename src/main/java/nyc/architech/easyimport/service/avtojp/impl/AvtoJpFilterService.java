package nyc.architech.easyimport.service.avtojp.impl;

import lombok.RequiredArgsConstructor;
import nyc.architech.easyimport.domain.enums.FilterType;
import nyc.architech.easyimport.domain.enums.Platform;
import nyc.architech.easyimport.repository.FilterRepository;
import nyc.architech.easyimport.service.FilterService;
import nyc.architech.easyimport.service.dto.auction.FilterDTO;
import nyc.architech.easyimport.service.mapper.FilterMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvtoJpFilterService implements FilterService {

    private final FilterRepository filterRepository;
    private final FilterMapper filterMapper;

    @Override
    public List<FilterDTO> findByPlatformAndTypeAndParentValue(Platform platform, FilterType filterType, String parentExternalId) {
        return
            filterMapper.toDto(filterRepository
                .findByPlatformEqualsAndTypeEqualsAndParentExternalIdEqualsOrderByNameAsc(platform, filterType, parentExternalId));
    }
}
