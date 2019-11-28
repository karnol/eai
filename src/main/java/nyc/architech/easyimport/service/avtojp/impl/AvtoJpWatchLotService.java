package nyc.architech.easyimport.service.avtojp.impl;

import lombok.RequiredArgsConstructor;
import nyc.architech.easyimport.domain.User;
import nyc.architech.easyimport.domain.WatchLot;
import nyc.architech.easyimport.service.WatchLotService;
import nyc.architech.easyimport.service.dto.WatchLotDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AvtoJpWatchLotService implements WatchLotService {

    private static final String OPEN_STATUS = "";

    @Override
    public Optional<List<WatchLotDTO>> findByCurrentUser() {
        return Optional.empty();
    }

    @Override
    public Optional<List<WatchLotDTO>> findByUser(Long id) {
        return Optional.empty();
    }

    @Override
    public WatchLotDTO addWatch(String externalId, User user) {
        return null;
    }

    @Override
    public WatchLotDTO deleteWatch(String externalId, User user) {
        return null;
    }

    @Override
    public WatchLotDTO deleteWatch(Long id) {
        return null;
    }

    @Override
    public void syncWithSource() {

    }

//    private final WatchLotRepository repository;
//    private final UserRepository userRepository;
//    private final AuctionService auctionLotService;
////    private final AuctionLotService auctionLotService;
//    private final SettingsService settingsService;

/*
    @Override
    public List<WatchLotDTO> findByCurrentUser() {
        return findByUserId(getLoggedUserId());
    }

    @Override
    public List<WatchLotDTO> findByUserId(Long userId) {
        return repository.findByUserId(userId)
                    .stream()
                    .map(WatchLotDTO::fromEntity)
                    .collect(Collectors.toList());
    }

    @Override
    public boolean updateWatchesWithOpenStatus() {
        List<WatchLot> watchLots = repository.findByStatus(OPEN_STATUS);
        List<WatchLot> watchLotsToUpdate = new ArrayList<>();

        Float usdToJpyRate = Float.parseFloat(settingsService.getSettingsByName(Settings.USD_TO_JPY_RATE).getValue());

        watchLots
            .forEach(watchLot -> {
                Optional<AuctionLotDTO> auctionLotDTO = auctionLotService.findById(watchLot.getExternalId());
                if (auctionLotDTO.isPresent()) {
                    AuctionLotDTO auctionLotDTO1 = auctionLotDTO.get();
                    if (!auctionLotDTO1.getStatus().equalsIgnoreCase(watchLot.getStatus())) {
                        watchLot.setStatus(auctionLotDTO1.getStatus());
                        if (!auctionLotDTO1.getFinishPrice().equals(0)) {
                            watchLot.addNewPrice(auctionLotDTO1.getFinishPrice(), usdToJpyRate);
                        } else if (!auctionLotDTO1.getPrice().equals(watchLot.getPriceOriginal())) {
                            watchLot.addNewPrice(auctionLotDTO1.getPrice(), usdToJpyRate);
                        }
                        watchLotsToUpdate.add(watchLot);
                    } else
                        if (!auctionLotDTO1.getPrice().equals(watchLot.getPriceOriginal())) {
                            watchLot.addNewPrice(auctionLotDTO1.getPrice(), usdToJpyRate);
                            watchLotsToUpdate.add(watchLot);
                        }
                }
            });

        repository.saveAll(watchLotsToUpdate);
        return true;
    }

    @Override
    public boolean addByExternalId(String externalId) {
        List<WatchLot> watchLots = repository.findByExternalId(externalId);
        if (!CollectionUtils.isEmpty(watchLots)) {
            return true;
        }
        Optional<AuctionLotDTO> auctionLotDTO = auctionLotService.findById(externalId);
        if (auctionLotDTO.isPresent()) {
            WatchLot watchLot = WatchLot.fromAuctionLotDTO(auctionLotDTO.get());
            watchLot.setUserId(getLoggedUserId());
            repository.save(watchLot);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeByExternalId(String externalId) {
        return false;
    }

    @Override
    public boolean removeById(Long id) {
        repository.deleteById(id);
        return true;
    }

    private Long getLoggedUserId() {
        return SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneWithAuthoritiesByLogin).get().getId();
    }
*/
}
