package isys.labs.staff.repository;

import isys.labs.staff.entity.EmployeePositionGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeePositionGradeRepository extends JpaRepository<EmployeePositionGrade, Long> {
    List<EmployeePositionGrade> findByEmployeeId(Long employeeId);
}
