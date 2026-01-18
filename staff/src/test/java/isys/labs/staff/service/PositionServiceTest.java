package isys.labs.staff.service;

import isys.labs.staff.client.HandBookClient;
import isys.labs.staff.dto.PositionFromHandBookDto;
import isys.labs.staff.entity.Position;
import isys.labs.staff.repository.PositionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PositionServiceTest {

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private HandBookClient handBookClient;

    @InjectMocks
    private PositionService positionService;

    @Test
    void syncAll_shouldSaveAllPositionsFromReferenceService() {
        PositionFromHandBookDto dto1 = new PositionFromHandBookDto();
        dto1.setId(1L);
        dto1.setName("Dev");
        dto1.setCode("DEV");

        PositionFromHandBookDto dto2 = new PositionFromHandBookDto();
        dto2.setId(2L);
        dto2.setName("QA");
        dto2.setCode("QA");

        when(handBookClient.getAllPositions()).thenReturn(List.of(dto1, dto2));

        positionService.syncAll();

        verify(positionRepository, times(2)).save(any(Position.class));
    }
}

