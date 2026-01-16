package isys.labs.staff.service;

import isys.labs.staff.dto.DepartmentDto;
import isys.labs.staff.entity.Department;
import isys.labs.staff.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public DepartmentService(DepartmentRepository departmentRepository,
                             ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    public List<DepartmentDto> getAll() {
        return departmentRepository.findAll().stream()
                .map(d -> modelMapper.map(d, DepartmentDto.class))
                .toList();
    }

    public DepartmentDto getById(Long id) {
        Department dep = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + id));
        return modelMapper.map(dep, DepartmentDto.class);
    }

    public DepartmentDto create(DepartmentDto dto) {
        if (departmentRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Department code already exists: " + dto.getCode());
        }
        Department entity = modelMapper.map(dto, Department.class);
        Department saved = departmentRepository.save(entity);
        return modelMapper.map(saved, DepartmentDto.class);
    }

    public DepartmentDto update(Long id, DepartmentDto dto) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + id));

        dto.setId(id);
        modelMapper.map(dto, existing);

        Department saved = departmentRepository.save(existing);
        return modelMapper.map(saved, DepartmentDto.class);
    }

    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }
}
