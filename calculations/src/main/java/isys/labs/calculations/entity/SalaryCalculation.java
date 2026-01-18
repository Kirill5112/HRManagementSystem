package isys.labs.calculations.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * расчеты зарплаты
 */
@Entity
@Table(
        name = "salary_calculations",
        indexes = {
                @Index(name = "idx_salary_calc_emp", columnList = "employee_id"),
                @Index(name = "idx_salary_calc_period", columnList = "period_start, period_end"),
                @Index(name = "idx_salary_calc_status", columnList = "status"),
                @Index(name = "uk_salary_calc", columnList = "employee_id, period_start, period_end", unique = true)
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryCalculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID сотрудника из сервиса 1
    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate;

    @Column(name = "taxes_amount", precision = 10, scale = 2)
    private BigDecimal taxesAmount;

    @Column(name = "social_contributions", precision = 10, scale = 2)
    private BigDecimal socialContributions = BigDecimal.ZERO;

    // ===== Результаты расчёта =====

    @Column(name = "gross_salary", nullable = false, precision = 10, scale = 2)
    private BigDecimal grossSalary;

    @Column(name = "bonuses", precision = 10, scale = 2)
    private BigDecimal bonuses = BigDecimal.ZERO;

    @Column(name = "deductions", precision = 10, scale = 2)
    private BigDecimal deductions = BigDecimal.ZERO;

    /**
     * чистая ЗП
     */
    @Column(name = "net_salary", precision = 10, scale = 2)
    private BigDecimal netSalary;

    // ===== Статус и метаданные =====

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private CalculationStatus status = CalculationStatus.CALCULATED;

    @UpdateTimestamp
    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Статусы расчёта зарплаты.
     */
    public enum CalculationStatus {
        CALCULATED,   // предварительно рассчитано
        VERIFIED,     // проверено бухгалтером
        APPROVED,     // утверждено
        PAID          // выплачено
    }
}
