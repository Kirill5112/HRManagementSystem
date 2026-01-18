package isys.labs.handbook.service;

import isys.labs.handbook.dto.BenefitCategoryDto;
import isys.labs.handbook.entity.BenefitCategory;
import isys.labs.handbook.repository.BenefitCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BenefitCategoryService {

    private final BenefitCategoryRepository benefitCategoryRepository;
    private final ModelMapper modelMapper;

    public List<BenefitCategoryDto> getAll() {
        return benefitCategoryRepository.findAll().stream()
        .map(p -> modelMapper.map(p, BenefitCategoryDto.class))
                .toList();
    }

    public BenefitCategoryDto getById(Long id) {
        BenefitCategory category = benefitCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Benefit category not found: " + id));
        return modelMapper.map(category, BenefitCategoryDto.class);
    }

    public BenefitCategoryDto create(BenefitCategoryDto category) {
        BenefitCategory entity = modelMapper.map(category, BenefitCategory.class);
        BenefitCategory saved = benefitCategoryRepository.save(entity);
        return modelMapper.map(saved, BenefitCategoryDto.class);
    }

    @Transactional
    public BenefitCategoryDto update(Long id, BenefitCategoryDto updated) {
        BenefitCategory existing = benefitCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Benefit category not found: "));
        updated.setId(id);
        modelMapper.map(updated, existing);

        BenefitCategory saved = benefitCategoryRepository.save(existing);
        return modelMapper.map(saved, BenefitCategoryDto.class);
    }

    public void delete(Long id) {
        benefitCategoryRepository.deleteById(id);
    }
}
