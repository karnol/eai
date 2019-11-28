package nyc.architech.easyimport.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class representation a root application controller for correct working in clouds
 */
@RestController
@RequestMapping("/")
public class RootResource {

    @GetMapping
    public ResponseEntity get() {
        return ResponseEntity.ok().build();
    }
}
