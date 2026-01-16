package isys.labs.calculations.repository;

import isys.labs.calculations.entity.SalaryCalculation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryCalculationRepository extends JpaRepository<SalaryCalculation, Long> {
}
