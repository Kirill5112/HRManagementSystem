package isys.labs.staff.service;

import isys.labs.staff.client.HandBookClient;
import isys.labs.staff.dto.PositionFromHandBookDto;
import isys.labs.staff.entity.Position;
import isys.labs.staff.repository.PositionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final HandBookClient handBookClient; // твой клиент к сервису 2

    /**
     * Полная синхронизация всех должностей из сервиса 2.
     */
    public void syncAll() {
        List<PositionFromHandBookDto> remotePositions = handBookClient.getAllPositions();
        for (PositionFromHandBookDto remote : remotePositions) {
            Position pos = positionRepository.findById(remote.getId())
                    .orElseGet(Position::new);

            pos.setId(remote.getId());
            pos.setName(remote.getName());
            pos.setCode(remote.getCode());
            pos.setSyncedAt(LocalDateTime.now());

            positionRepository.save(pos);
        }
    }

    /**
     * Синхронизация одной должности по ID
     */
    public Position syncOne(Long positionId) {
        PositionFromHandBookDto remote = handBookClient.getPositionById(positionId);

        if (remote == null) {
            throw new IllegalArgumentException("Position not found in handbook service: " + positionId);
        }

        Position pos = positionRepository.findById(positionId)
                .orElseGet(Position::new);

        pos.setId(remote.getId());
        pos.setName(remote.getName());
        pos.setCode(remote.getCode());
        pos.setSyncedAt(LocalDateTime.now());

        positionRepository.save(pos);
        return pos;
    }
}