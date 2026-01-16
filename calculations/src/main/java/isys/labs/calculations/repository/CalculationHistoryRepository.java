package isys.labs.calculations.repository;

import isys.labs.calculations.entity.CalculationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationHistoryRepository extends JpaRepository<CalculationHistory, Long> {
}
