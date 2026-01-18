package isys.labs.staff.job;

import isys.labs.staff.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PositionCacheSyncJob {

    private final PositionService positionService;

    //Синхронизация должностей раз в час
    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void syncPositions() {
        positionService.syncAll();
    }
}
