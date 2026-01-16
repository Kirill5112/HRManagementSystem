package isys.labs.staff.init;

import isys.labs.staff.service.PositionCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PositionCacheInitializer {

    private final PositionCacheService positionCacheService;

    @EventListener(ApplicationReadyEvent.class)
    public void initCache() {
        positionCacheService.syncAll();
    }
}
