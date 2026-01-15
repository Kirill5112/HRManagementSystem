package isys.labs.handbook.repository;

import isys.labs.handbook.entity.Grade;
import isys.labs.handbook.entity.Position;
import isys.labs.handbook.entity.PositionGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionGradeRepository extends JpaRepository<PositionGrade, Long> {

    List<PositionGrade> findByPosition(Position position);

    List<PositionGrade> findByGrade(Grade grade);

    Optional<PositionGrade> findByPositionAndGrade(Position position, Grade grade);
}