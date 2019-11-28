package nyc.architech.easyimport.domain;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Settings implements Serializable {

    //TODO: maybe use it as OrderSettings ?! or just drop it...
    public static final String USD_TO_JPY_RATE = "USD_TO_JPY";

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String value;

    @Column
    private String extra;//TODO -> additionalInfo

    //
    @Column
    private Long platformId;

    @Column
    @LastModifiedDate
    private Instant modified = Instant.now();
}
