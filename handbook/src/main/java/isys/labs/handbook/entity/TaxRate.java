package isys.labs.handbook.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * налоговые ставки
 */
@Entity
@Table(
        name = "tax_rates",
        indexes = {
                @Index(name = "idx_tax_rates_valid", columnList = "valid_from, valid_to")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(precision = 4, scale = 2, nullable = false)
    private BigDecimal rate;

    private String description;

    //дата начала действия
    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    //дата окончания действия
    @Column(name = "valid_to")
    private LocalDate validTo;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
