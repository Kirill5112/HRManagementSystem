package isys.labs.handbook.service;

import isys.labs.handbook.dto.PositionDto;
import isys.labs.handbook.entity.Position;
import isys.labs.handbook.repository.PositionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository positionRepository;
    private final ModelMapper modelMapper;

    public List<PositionDto> getAll() {
        return positionRepository.findAll().stream()
                .map(p -> modelMapper.map(p, PositionDto.class))
                .toList();
    }

    public PositionDto getById(Long id) {
        Position entity = positionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Position not found: " + id));
        return modelMapper.map(entity, PositionDto.class);
    }

    public PositionDto create(PositionDto dto) {
        Position entity = modelMapper.map(dto, Position.class);
        Position saved = positionRepository.save(entity);
        return modelMapper.map(saved, PositionDto.class);
    }

    @Transactional
    public PositionDto update(Long id, PositionDto dto) {
        Position existing = positionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Position not found: " + id));

        dto.setId(id);
        modelMapper.map(dto, existing);

        Position saved = positionRepository.save(existing);
        return modelMapper.map(saved, PositionDto.class);
    }

    public void delete(Long id) {
        positionRepository.deleteById(id);
    }
}
