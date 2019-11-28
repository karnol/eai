package nyc.architech.easyimport.domain;

import lombok.Data;
import nyc.architech.easyimport.domain.enums.Currency;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class ExchangeRate implements Serializable {

    public static final String USD_TO_JPY_RATE = "USD_TO_JPY";
    public static final String ORDER_EXTRA_1 = "USD_TO_JPY";

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Currency from;

    @Column
    @Enumerated(value = EnumType.STRING)
    private Currency to;

    @Column
    private Double rate;

    @Column
    @LastModifiedDate
    private Instant modified = Instant.now();
}
