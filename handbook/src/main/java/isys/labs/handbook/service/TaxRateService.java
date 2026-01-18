package isys.labs.handbook.service;

import isys.labs.handbook.dto.TaxRateDto;
import isys.labs.handbook.entity.TaxRate;
import isys.labs.handbook.repository.TaxRateRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TaxRateService {

    private final TaxRateRepository taxRateRepository;
    private final ModelMapper modelMapper;

    public TaxRateService(TaxRateRepository taxRateRepository, ModelMapper modelMapper) {
        this.taxRateRepository = taxRateRepository;
        this.modelMapper = modelMapper;
    }

    public List<TaxRateDto> getAll() {
        return taxRateRepository.findAll().stream()
                .map(taxRate -> modelMapper.map(taxRate, TaxRateDto.class))
                .toList();
    }

    public TaxRateDto getById(Long id) {
        TaxRate taxRate = taxRateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tax rate not found: " + id));
        return modelMapper.map(taxRate, TaxRateDto.class);
    }

    public TaxRateDto create(TaxRateDto dto) {
        TaxRate taxRate = modelMapper.map(dto, TaxRate.class);
        TaxRate saved = taxRateRepository.save(taxRate);
        return modelMapper.map(saved, TaxRateDto.class);
    }

    public TaxRateDto update(Long id, TaxRateDto updated) {
        TaxRate existing = taxRateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tax rate not found: " + id));
        updated.setId(id);
        modelMapper.map(updated, existing);
        TaxRate saved = taxRateRepository.save(existing);
        return modelMapper.map(saved, TaxRateDto.class);
    }

    public void delete(Long id) {
        taxRateRepository.deleteById(id);
    }

    /**
     * Получить актуальную ставку на дату.
     */
    public Optional<TaxRateDto> getEffectiveTaxRate(LocalDate date) {
        List<TaxRate> rates = taxRateRepository.findByValidFromLessThanEqualAndValidToGreaterThanEqual(date, date);
        Optional<TaxRate> effective = Optional.empty();
        if (!rates.isEmpty()) {
            effective = rates.stream()
                    .max(Comparator.comparing(TaxRate::getValidFrom));
        }
        // если valid_to = null — бессрочные
        List<TaxRate> openEnded = taxRateRepository.findByValidToIsNull();
        if (!openEnded.isEmpty()) {
            effective = openEnded.stream()
                    .filter(r -> !r.getValidFrom().isAfter(date))
                    .max(Comparator.comparing(TaxRate::getValidFrom));
        }
        return effective.map(rate -> modelMapper.map(rate, TaxRateDto.class));
    }
}