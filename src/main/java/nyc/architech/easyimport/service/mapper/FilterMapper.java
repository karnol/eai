package nyc.architech.easyimport.service.mapper;

import nyc.architech.easyimport.domain.Filter;
import nyc.architech.easyimport.service.dto.auction.FilterDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class FilterMapper implements EntityMapper<FilterDTO, Filter> {
}
