package isys.labs.calculations.repository;

import isys.labs.calculations.entity.CashBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashBalanceRepository extends JpaRepository<CashBalance, Long> {
    Optional<CashBalance> findByDepartmentId(Long departmentId);
}
