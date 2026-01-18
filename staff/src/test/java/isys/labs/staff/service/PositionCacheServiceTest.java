package isys.labs.staff.service;

import isys.labs.staff.client.HandBookClient;
import isys.labs.staff.dto.PositionDtoFromHandBook;
import isys.labs.staff.entity.Position;
import isys.labs.staff.repository.PositionCacheRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionCacheServiceTest {

    @Mock
    private PositionCacheRepository positionCacheRepository;

    @Mock
    private HandBookClient handBookClient;

    @InjectMocks
    private PositionCacheService positionCacheService;

    @Test
    void syncAll_shouldSaveAllPositionsFromReferenceService() {
        PositionDtoFromHandBook dto1 = new PositionDtoFromHandBook();
        dto1.setId(1L);
        dto1.setName("Dev");
        dto1.setCode("DEV");

        PositionDtoFromHandBook dto2 = new PositionDtoFromHandBook();
        dto2.setId(2L);
        dto2.setName("QA");
        dto2.setCode("QA");

        when(handBookClient.getAllPositions()).thenReturn(List.of(dto1, dto2));

        positionCacheService.syncAll();

        verify(positionCacheRepository, times(2)).save(any(Position.class));
    }
}

