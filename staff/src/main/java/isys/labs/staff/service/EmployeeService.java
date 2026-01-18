package isys.labs.staff.service;

import isys.labs.staff.client.HandBookClient;
import isys.labs.staff.dto.BenefitCategoryDtoFromHandbook;
import isys.labs.staff.dto.EmployeeDto;
import isys.labs.staff.entity.Department;
import isys.labs.staff.entity.Employee;
import isys.labs.staff.entity.EmployeeStatus;
import isys.labs.staff.entity.Position;
import isys.labs.staff.repository.DepartmentRepository;
import isys.labs.staff.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionCacheService positionCacheService;
    private final ModelMapper modelMapper;
    private final HandBookClient handBookClient;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository, PositionCacheService positionCacheService,
                           ModelMapper modelMapper, HandBookClient handBookClient) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.positionCacheService = positionCacheService;
        this.modelMapper = modelMapper;
        this.handBookClient = handBookClient;
    }

    public EmployeeDto getById(Long id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
        return modelMapper.map(emp, EmployeeDto.class);
    }

    public EmployeeDto create(EmployeeDto dto) {
        Long benefitCategoryId = dto.getBenefitCategoryId();
        BenefitCategoryDtoFromHandbook remote = handBookClient.getBenefitCategory(benefitCategoryId);
        if (remote == null) {
            throw new IllegalArgumentException("BenefitCategory not found in handbook service: " + benefitCategoryId);
        }
        Employee emp = mapDtoToEntity(dto);
        Employee saved = employeeRepository.save(emp);
        return modelMapper.map(saved, EmployeeDto.class);
    }

    public EmployeeDto update(Long id, EmployeeDto dto) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));


        Employee updatedData = mapDtoToEntity(dto);
        existing.setFirstName(updatedData.getFirstName());
        existing.setLastName(updatedData.getLastName());
        existing.setEmail(updatedData.getEmail());
        existing.setDepartment(updatedData.getDepartment());
        existing.setStatus(updatedData.getStatus());
        existing.setHireDate(updatedData.getHireDate());
        existing.setBenefitCategoryId(updatedData.getBenefitCategoryId());

        Employee saved = employeeRepository.save(existing);
        return modelMapper.map(saved, EmployeeDto.class);
    }

    public EmployeeDto assignPositions(Long employeeId, List<Long> positionIds) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + employeeId));

        Set<Position> positions = new HashSet<>();

        for (Long positionId : positionIds) {
            positions.add(positionCacheService.syncOne(positionId));
        }

        employee.setPositions(positions);
        Employee saved = employeeRepository.save(employee);

        return modelMapper.map(saved, EmployeeDto.class);
    }


    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    private Employee mapDtoToEntity(EmployeeDto dto) {
        Employee emp = modelMapper.map(dto, Employee.class);

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + dto.getDepartmentId()));
        emp.setDepartment(department);

        emp.setStatus(EmployeeStatus.valueOf(dto.getStatus())); // ACTIVE/ON_LEAVE/etc.

        return emp;
    }

    public Page<EmployeeDto> getEmployees(Pageable pageable) {
        Page<Employee> page = employeeRepository.findAll(pageable);
        return page.map(emp -> modelMapper.map(emp, EmployeeDto.class));
    }
}
