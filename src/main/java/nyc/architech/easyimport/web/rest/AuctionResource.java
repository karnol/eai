package nyc.architech.easyimport.web.rest;

import lombok.RequiredArgsConstructor;
import nyc.architech.easyimport.service.AuctionService;
import nyc.architech.easyimport.service.dto.auction.AuctionSearchDTO;
import nyc.architech.easyimport.service.dto.auction.LotDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-controller for search through AuctionLots. Actually it has ONLY "reading" ability.
 * Because we are getting AuctionLots "on fly" by REST from others web-based sources.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auction-lots", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AuctionResource {

    private final AuctionService auctionService;

    @GetMapping
    public ResponseEntity<Page<LotDTO>> search(@Validated AuctionSearchDTO params, Pageable pageable) {
        return ResponseEntity.ok(auctionService.search(params, pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<LotDTO> findById(@PathVariable String id) {
        return auctionService.findById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
