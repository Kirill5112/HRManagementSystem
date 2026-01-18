package isys.labs.staff.service;

import isys.labs.staff.client.HandBookClient;
import isys.labs.staff.dto.BenefitCategoryFromHandbookDto;
import isys.labs.staff.dto.EmployeeDto;
import isys.labs.staff.entity.Department;
import isys.labs.staff.entity.Employee;
import isys.labs.staff.repository.DepartmentRepository;
import isys.labs.staff.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private HandBookClient handBookClient;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void create_whenBenefitCategoryExists_shouldSaveEmployee() {
        EmployeeDto dto = new EmployeeDto();
        dto.setBenefitCategoryId(1L);
        dto.setDepartmentId(1L);
        dto.setStatus("ACTIVE");

        BenefitCategoryFromHandbookDto benefit = new BenefitCategoryFromHandbookDto();
        when(handBookClient.getBenefitCategory(1L)).thenReturn(benefit);

        Department dept = new Department();
        dept.setId(1L);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));

        Employee empMapped = new Employee();
        when(modelMapper.map(dto, Employee.class)).thenReturn(empMapped);

        Employee empSaved = new Employee();
        empSaved.setId(10L);
        when(employeeRepository.save(empMapped)).thenReturn(empSaved);

        when(modelMapper.map(empSaved, EmployeeDto.class)).thenReturn(dto);

        EmployeeDto result = employeeService.create(dto);

        assertEquals(dto, result);
        verify(handBookClient).getBenefitCategory(1L);
        verify(departmentRepository).findById(1L);
        verify(employeeRepository).save(empMapped);
    }

    @Test
    void create_whenBenefitCategoryMissing_shouldThrow() {
        EmployeeDto dto = new EmployeeDto();
        dto.setBenefitCategoryId(1L);

        when(handBookClient.getBenefitCategory(1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> employeeService.create(dto));
    }
}

