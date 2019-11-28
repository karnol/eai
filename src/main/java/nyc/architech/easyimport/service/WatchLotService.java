package nyc.architech.easyimport.service;

import nyc.architech.easyimport.domain.User;
import nyc.architech.easyimport.service.dto.WatchLotDTO;

import java.util.List;
import java.util.Optional;

public interface WatchLotService {

    Optional<List<WatchLotDTO>> findByCurrentUser();
    Optional<List<WatchLotDTO>> findByUser(Long id);

    // create Lot for addCondition sourceLot to watch list
    WatchLotDTO addWatch(String externalId, User user);
    WatchLotDTO deleteWatch(String externalId, User user);
    WatchLotDTO deleteWatch(Long id);

    void syncWithSource();
}
