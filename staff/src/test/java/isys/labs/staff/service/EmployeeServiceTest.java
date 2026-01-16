package isys.labs.staff.service;

import isys.labs.staff.dto.EmployeeDto;
import isys.labs.staff.entity.Employee;
import isys.labs.staff.entity.Position;
import isys.labs.staff.repository.EmployeeRepository;
import isys.labs.staff.repository.PositionCacheRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PositionCacheRepository positionCacheRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void assignPositions_shouldSetPositionsFromCache() {
        Long employeeId = 10L;
        Long posId = 1L;

        Employee employee = new Employee();
        employee.setId(employeeId);

        Position position = new Position();
        position.setId(posId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(positionCacheRepository.findAllById(List.of(posId))).thenReturn(List.of(position));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        EmployeeDto dto = new EmployeeDto();
        when(modelMapper.map(any(Employee.class), eq(EmployeeDto.class))).thenReturn(dto);

        EmployeeDto result = employeeService.assignPositions(employeeId, List.of(posId));

        assertNotNull(result);
        assertEquals(1, employee.getPositions().size());
        assertTrue(employee.getPositions().contains(position));
    }
}

