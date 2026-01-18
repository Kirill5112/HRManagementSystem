package isys.labs.calculations.service;

import isys.labs.calculations.dto.CashBalanceDto;
import isys.labs.calculations.entity.CashBalance;
import isys.labs.calculations.repository.CashBalanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CashBalanceService {

    private final CashBalanceRepository cashBalanceRepository;
    private final ModelMapper modelMapper;

    public List<CashBalanceDto> getAll(){
        return cashBalanceRepository.findAll().stream().map(
                cashBalance -> modelMapper.map(cashBalance, CashBalanceDto.class))
                .toList();
    }

    public CashBalanceDto getCashBalanceByDepartmentId(Long departmentId) {
        CashBalance cashBalance = cashBalanceRepository.findByDepartmentId(departmentId)
                .orElseThrow(() -> new IllegalStateException("Баланс не найден. DepartmentId: " + departmentId));
        return modelMapper.map(cashBalance, CashBalanceDto.class);
    }

    public void deleteCashBalance(Long id) {
        cashBalanceRepository.deleteById(id);
    }

    public CashBalanceDto createCashBalance(CashBalanceDto dto) {
        CashBalance entity = modelMapper.map(dto, CashBalance.class);
        CashBalance saved = cashBalanceRepository.save(entity);
        return modelMapper.map(saved, CashBalanceDto.class);
    }

    @Transactional
    public CashBalanceDto updateCashBalance(Long id, CashBalanceDto dto) {
        CashBalance cashBalance = cashBalanceRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("CashBalance not found, id: " + id));
        dto.setId(id);
        modelMapper.map(dto, cashBalance);
        CashBalance saved = cashBalanceRepository.save(cashBalance);
        return modelMapper.map(saved, CashBalanceDto.class);
    }
}
