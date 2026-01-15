package isys.labs.calculations.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Агрегированный отчёт по зарплате за период по отделу.
 */
@Entity
@Table(
        name = "salary_reports",
        indexes = {
                @Index(name = "idx_report_period", columnList = "period_start, period_end"),
                @Index(name = "idx_report_dept", columnList = "department_id"),
                @Index(name = "uk_report", columnList = "period_start, period_end, department_id", unique = true)
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Период отчёта
    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    // Отдел из сервиса 1 (храним только ID)
    @Column(name = "department_id")
    private Long departmentId;

    // Агрегаты
    @Column(name = "total_gross_salary", precision = 15, scale = 2)
    private BigDecimal totalGrossSalary;

    @Column(name = "total_net_salary", precision = 15, scale = 2)
    private BigDecimal totalNetSalary;

    @Column(name = "total_taxes", precision = 15, scale = 2)
    private BigDecimal totalTaxes;

    @Column(name = "avg_salary", precision = 10, scale = 2)
    private BigDecimal avgSalary;

    @Column(name = "employee_count")
    private Integer employeeCount;

    // Метаданные
    @CreationTimestamp
    @Column(name = "generated_at", nullable = false, updatable = false)
    private LocalDateTime generatedAt;
}
