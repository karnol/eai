package nyc.architech.easyimport.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nyc.architech.easyimport.domain.enums.FilterType;
import nyc.architech.easyimport.domain.enums.Platform;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Filter {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String externalId;

    @Column
    private String name;

    @Column
    private String parentExternalId;

    @Column
    @Enumerated(EnumType.STRING)
    private FilterType type;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Platform platform;

    @Column
    private String additionalInfo;
}
