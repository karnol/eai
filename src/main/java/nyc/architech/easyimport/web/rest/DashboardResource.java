package nyc.architech.easyimport.web.rest;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nyc.architech.easyimport.domain.WatchLot;
import nyc.architech.easyimport.security.AuthoritiesConstants;
import nyc.architech.easyimport.service.WatchLotService;
import nyc.architech.easyimport.service.dto.WatchLotDTO;
import nyc.architech.easyimport.service.dto.auction.LotDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/dashboard", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class DashboardResource {

    private final WatchLotService watchLotService;

    @GetMapping("/watch")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<WatchLotDTO>> find() {
        return watchLotService.findByCurrentUser()
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/watch")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity addWatch(@RequestParam(name = "external_id") String externalId) {
        log.debug("REST request to add Lot to WatchList by externalId - {}", externalId);

        //TODO
        return null;
//        return watchLotService.addByExternalId(externalId) ?
//                new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/watch/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.USER + "\")")
    public ResponseEntity<List<WatchLotDTO>> delete(@PathVariable Long id) {
        //TODO:
        return null;
        //        return watchLotService.removeById(id) ? new ResponseEntity<>(HttpStatus.OK)
//                                                        : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
