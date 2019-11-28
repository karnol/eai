package nyc.architech.easyimport.service.avtojp;

import nyc.architech.easyimport.service.AuctionService;
import nyc.architech.easyimport.service.DictionaryService;
import nyc.architech.easyimport.service.dto.auction.LotDTO;

import java.util.List;

public interface AutoJpService extends AuctionService, DictionaryService {

    List<LotDTO> find(Integer count);
}
