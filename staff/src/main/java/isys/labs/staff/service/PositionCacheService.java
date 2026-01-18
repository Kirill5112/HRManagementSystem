package isys.labs.staff.service;

import isys.labs.staff.client.HandBookClient;
import isys.labs.staff.dto.PositionDtoFromHandBook;
import isys.labs.staff.entity.Position;
import isys.labs.staff.repository.PositionCacheRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PositionCacheService {

    private final PositionCacheRepository positionCacheRepository;
    private final HandBookClient handBookClient; // твой клиент к сервису 2

    public PositionCacheService(PositionCacheRepository positionCacheRepository,
                                HandBookClient handBookClient) {
        this.positionCacheRepository = positionCacheRepository;
        this.handBookClient = handBookClient;
    }

    /**
     * Полная синхронизация всех должностей из сервиса 2.
     */
    public void syncAll() {
        List<PositionDtoFromHandBook> remotePositions = handBookClient.getAllPositions();
        for (PositionDtoFromHandBook remote : remotePositions) {
            Position pos = positionCacheRepository.findById(remote.getId())
                    .orElseGet(Position::new);

            pos.setId(remote.getId());
            pos.setName(remote.getName());
            pos.setCode(remote.getCode());
            pos.setSyncedAt(LocalDateTime.now());

            positionCacheRepository.save(pos);
        }
    }

    /**
     * Синхронизация одной должности по ID
     */
    public Position syncOne(Long positionId) {
        PositionDtoFromHandBook remote = handBookClient.getPositionById(positionId);

        if (remote == null) {
            throw new IllegalArgumentException("Position not found in handbook service: " + positionId);
        }

        Position pos = positionCacheRepository.findById(positionId)
                .orElseGet(Position::new);

        pos.setId(remote.getId());
        pos.setName(remote.getName());
        pos.setCode(remote.getCode());
        pos.setSyncedAt(LocalDateTime.now());

        positionCacheRepository.save(pos);
        return pos;
    }
}