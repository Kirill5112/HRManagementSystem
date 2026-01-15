package isys.labs.handbook.entity;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Связь между должностью и грейдом.
 * Одна должность может иметь несколько грейдов (Junior/Middle/Senior),
 * и один грейд может относиться к разным должностям.
 */
@Entity
@Table(
        name = "position_grades",
        indexes = {
                @Index(name = "idx_pos_grade_pos", columnList = "position_id"),
                @Index(name = "idx_pos_grade_grade", columnList = "grade_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_pos_grade",
                        columnNames = {"position_id", "grade_id"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Должность (Position)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    // Грейд (Grade)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id", nullable = false)
    private Grade grade;

    // Множитель к базовой ставке (например, 1.0, 1.2, 1.5)
    @Column(name = "salary_multiplier", precision = 4, scale = 2, nullable = false)
    private BigDecimal salaryMultiplier = BigDecimal.ONE;
}
