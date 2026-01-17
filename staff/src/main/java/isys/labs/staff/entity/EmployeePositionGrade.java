package isys.labs.staff.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee_position_grade",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_emp_pos",
                columnNames = {"employee_id", "position_id"}
        ))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePositionGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId; // из сервиса 1

    @Column(name = "position_id", nullable = false)
    private Long positionId; // ID Position из сервиса 2

    @Column(name = "grade_id", nullable = false)
    private Long gradeId; // ID Grade из сервиса 2
}
