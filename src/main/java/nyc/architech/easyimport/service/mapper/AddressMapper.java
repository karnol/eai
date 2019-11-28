package nyc.architech.easyimport.service.mapper;

import nyc.architech.easyimport.domain.Address;
import nyc.architech.easyimport.service.dto.AddressDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDTO addressToAddressDTO(Address address);

    List<AddressDTO> addressListToAddressDTOList(List<Address> addressList);

    Address addressDtoToAddress(AddressDTO addressDTO);

    List<Address> addressDtoListToAddressList(List<AddressDTO> addressDtoList);
}
