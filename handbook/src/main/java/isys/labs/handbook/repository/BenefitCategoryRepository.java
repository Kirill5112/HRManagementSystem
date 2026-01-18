package isys.labs.handbook.repository;

import isys.labs.handbook.entity.BenefitCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BenefitCategoryRepository extends JpaRepository<BenefitCategory, Long> {
}
