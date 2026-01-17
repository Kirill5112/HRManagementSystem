package isys.labs.calculations.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * история шагов расчета
 */
@Entity
@Table(
        name = "calculation_history",
        indexes = {
                @Index(name = "idx_calc_hist_calc", columnList = "calculation_id"),
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculation_id", nullable = false)
    private SalaryCalculation calculation;

    @Column(nullable = false)
    private String stepName;

    private Integer stepOrder;

    @Column(name = "input_data", columnDefinition = "text")
    private String inputData;

    @Column(name = "output_data", columnDefinition = "text")
    private String outputData;


    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
