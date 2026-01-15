package isys.labs.handbook.repository;

import isys.labs.handbook.entity.BenefitCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BenefitCategoryRepository extends JpaRepository<BenefitCategory, Long> {

    Optional<BenefitCategory> findByName(String name);
}
