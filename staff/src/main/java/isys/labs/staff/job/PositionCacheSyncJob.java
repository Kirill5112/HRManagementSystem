package isys.labs.staff.job;

import isys.labs.staff.service.PositionCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PositionCacheSyncJob {

    private final PositionCacheService positionCacheService;

    //Синхронизация должностей раз в час
    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void syncPositions() {
        positionCacheService.syncAll();
    }
}
