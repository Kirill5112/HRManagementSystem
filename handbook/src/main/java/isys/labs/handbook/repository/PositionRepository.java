package isys.labs.handbook.repository;

import isys.labs.handbook.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {

    Optional<Position> findByCode(String code);

    Optional<Position> findByName(String name);

    List<Position> findByNameContainingIgnoreCase(String query);
}
