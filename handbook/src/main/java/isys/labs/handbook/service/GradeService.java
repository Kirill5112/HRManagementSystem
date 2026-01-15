package isys.labs.handbook.service;

import isys.labs.handbook.dto.GradeDto;
import isys.labs.handbook.entity.Grade;
import isys.labs.handbook.repository.GradeRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class GradeService {

    private final GradeRepository gradeRepository;
    private final ModelMapper modelMapper;

    public GradeService(GradeRepository gradeRepository, ModelMapper modelMapper) {
        this.gradeRepository = gradeRepository;
        this.modelMapper = modelMapper;
    }

    public List<GradeDto> getAll() {
        return gradeRepository.findAll().stream()
                .map(grade -> modelMapper.map(grade, GradeDto.class))
                .toList();
    }

    public GradeDto getById(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("GradeDto not found: " + id));
        return modelMapper.map(grade, GradeDto.class);
    }

    public GradeDto create(GradeDto dto) {
        Grade grade = modelMapper.map(dto, Grade.class);
        Grade saved = gradeRepository.save(grade);
        return modelMapper.map(saved, GradeDto.class);
    }

    public GradeDto update(Long id, GradeDto updated) {
        Grade existing = gradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("GradeDto not found: " + id));
        updated.setId(id);
        modelMapper.map(updated, existing);
        Grade saved = gradeRepository.save(existing);
        return modelMapper.map(saved, GradeDto.class);
    }

    public void delete(Long id) {
        gradeRepository.deleteById(id);
    }
}
