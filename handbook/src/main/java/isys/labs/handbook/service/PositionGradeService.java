package isys.labs.handbook.service;

import isys.labs.handbook.dto.PositionGradeDto;
import isys.labs.handbook.entity.Grade;
import isys.labs.handbook.entity.Position;
import isys.labs.handbook.entity.PositionGrade;
import isys.labs.handbook.repository.GradeRepository;
import isys.labs.handbook.repository.PositionGradeRepository;
import isys.labs.handbook.repository.PositionRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PositionGradeService {

    private final PositionGradeRepository positionGradeRepository;
    private final PositionRepository positionRepository;
    private final GradeRepository gradeRepository;
    private final ModelMapper modelMapper;

    public PositionGradeService(PositionGradeRepository positionGradeRepository,
                                PositionRepository positionRepository,
                                GradeRepository gradeRepository, ModelMapper modelMapper) {
        this.positionGradeRepository = positionGradeRepository;
        this.positionRepository = positionRepository;
        this.gradeRepository = gradeRepository;
        this.modelMapper = modelMapper;
    }

    public List<PositionGradeDto> getByPosition(Long positionId) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Position not found: " + positionId));
        return positionGradeRepository.findByPosition(position).stream()
                .map(positionGrade -> modelMapper.map(positionGrade, PositionGradeDto.class))
                .toList();
    }

    public List<PositionGradeDto> getByGrade(Long gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new IllegalArgumentException("Grade not found: " + gradeId));
        return positionGradeRepository.findByGrade(grade).stream()
                .map(positionGrade -> modelMapper.map(positionGrade, PositionGradeDto.class))
                .toList();
    }

    public PositionGradeDto assignGradeToPosition(Long positionId, Long gradeId, BigDecimal multiplier) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Position not found: " + positionId));
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new IllegalArgumentException("Grade not found: " + gradeId));

        PositionGrade positionGrade = positionGradeRepository.findByPositionAndGrade(position, grade)
                .orElseGet(() -> {
                    PositionGrade pg = new PositionGrade();
                    pg.setPosition(position);
                    pg.setGrade(grade);
                    pg.setSalaryMultiplier(multiplier);
                    return positionGradeRepository.save(pg);
                });
        return modelMapper.map(positionGrade, PositionGradeDto.class);
    }

    public void removeGradeFromPosition(Long positionGradeId) {
        positionGradeRepository.deleteById(positionGradeId);
    }
}
