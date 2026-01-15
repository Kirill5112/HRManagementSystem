package isys.labs.handbook.repository;

import isys.labs.handbook.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    Optional<Grade> findByLevel(Integer level);

    Optional<Grade> findByName(String name);
}
