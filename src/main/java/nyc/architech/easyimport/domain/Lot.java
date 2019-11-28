package nyc.architech.easyimport.domain;

import lombok.Data;

import javax.persistence.ManyToMany;
import java.util.Set;

@Data
public class Lot {

    @ManyToMany
    private Set<User> watchers;
}
