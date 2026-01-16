package isys.labs.staff.service;

import isys.labs.staff.client.ReferenceClient;
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
    private final ReferenceClient referenceClient; // твой клиент к сервису 2

    public PositionCacheService(PositionCacheRepository positionCacheRepository,
                                ReferenceClient referenceClient) {
        this.positionCacheRepository = positionCacheRepository;
        this.referenceClient = referenceClient;
    }

    /**
     * Полная синхронизация всех должностей из сервиса 2.
     */
    public void syncAll() {
        List<PositionDtoFromHandBook> remotePositions = referenceClient.getAllPositions();
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
     * Синхронизация одной должности по ID (например, при создании/обновлении в сервисе 2).
     */
    public void syncOne(Long positionId) {
        PositionDtoFromHandBook remote = referenceClient.getPositionById(positionId);

        Position pos = positionCacheRepository.findById(positionId)
                .orElseGet(Position::new);

        pos.setId(remote.getId());
        pos.setName(remote.getName());
        pos.setCode(remote.getCode());
        pos.setSyncedAt(LocalDateTime.now());

        positionCacheRepository.save(pos);
    }
}