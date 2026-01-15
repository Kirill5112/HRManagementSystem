package isys.labs.handbook.repository;

import isys.labs.handbook.entity.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {

    // Все ставки, актуальные на конкретную дату
    List<TaxRate> findByValidFromLessThanEqualAndValidToGreaterThanEqual(LocalDate from, LocalDate to);

    List<TaxRate> findByValidToIsNull();
}
