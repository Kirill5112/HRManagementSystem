package isys.labs.staff.repository;

import isys.labs.staff.entity.Employee;
import isys.labs.staff.entity.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByDepartmentId(Long departmentId);

    List<Employee> findByStatus(EmployeeStatus status);

    List<Employee> findByLastNameContainingIgnoreCase(String lastNamePart);
}
