package nyc.architech.easyimport.service;

import nyc.architech.easyimport.service.dto.auction.AuctionSearchDTO;
import nyc.architech.easyimport.service.dto.auction.LotDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AuctionService {

    Page<LotDTO> search(AuctionSearchDTO searchDTO, Pageable pageable);
    Optional<LotDTO> findById(String id);
}
