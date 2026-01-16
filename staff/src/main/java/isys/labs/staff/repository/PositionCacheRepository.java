package isys.labs.staff.repository;

import isys.labs.staff.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionCacheRepository extends JpaRepository<Position, Long> {

    Optional<Position> findByCode(String code);
}
