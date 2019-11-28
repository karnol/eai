package nyc.architech.easyimport.web.rest;

import lombok.RequiredArgsConstructor;
import nyc.architech.easyimport.domain.enums.FilterType;
import nyc.architech.easyimport.domain.enums.Platform;
import nyc.architech.easyimport.service.FilterService;
import nyc.architech.easyimport.service.dto.auction.FilterDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class FilterResource {

    private final FilterService filterService;

    @GetMapping("/platform")
    public ResponseEntity<List<Platform>> getPlatforms() {
        return ResponseEntity.ok(Arrays.asList(Platform.values()));
    }

    @GetMapping(value = "/filter/{platform}/{filterType}")
    public ResponseEntity<List<FilterDTO>> findByType(@PathVariable Platform platform,
                                                      @PathVariable FilterType filterType,
                                                      @RequestParam(name = "parent", required = false) String parentExternalId) {
        return ResponseEntity.ok(filterService.findByPlatformAndTypeAndParentValue(platform, filterType, parentExternalId));
    }
}
