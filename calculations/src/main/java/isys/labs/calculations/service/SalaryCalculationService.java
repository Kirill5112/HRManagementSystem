package isys.labs.calculations.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import isys.labs.calculations.dto.SalaryCalculationEvent;
import isys.labs.calculations.entity.CalculationHistory;
import isys.labs.calculations.entity.SalaryCalculation;
import isys.labs.calculations.repository.CalculationHistoryRepository;
import isys.labs.calculations.repository.SalaryCalculationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SalaryCalculationService {

    private final SalaryCalculationRepository salaryCalculationRepository;
    private final CalculationHistoryRepository calculationHistoryRepository;

    @Transactional
    public void handleCalculationEvent(SalaryCalculationEvent event) {
        SalaryCalculation calc = new SalaryCalculation();
        calc.setEmployeeId(event.getEmployeeId());
        calc.setPeriodStart(event.getPeriodStart());
        calc.setPeriodEnd(event.getPeriodEnd());
        calc.setGrossSalary(event.getGrossSalary());
        calc.setBonuses(event.getBonuses());
        calc.setDeductions(event.getDeductions());

        BigDecimal taxRate = new BigDecimal("0.13");
        BigDecimal taxableBase = event.getGrossSalary()
                .add(event.getBonuses())
                .subtract(event.getDeductions());

        BigDecimal taxes = taxableBase.multiply(taxRate);
        BigDecimal social = taxableBase.multiply(new BigDecimal("0.30"));
        BigDecimal net = taxableBase.subtract(taxes).subtract(social);

        calc.setTaxRate(taxRate);
        calc.setTaxesAmount(taxes);
        calc.setSocialContributions(social);
        calc.setNetSalary(net);
        calc.setStatus(SalaryCalculation.CalculationStatus.CALCULATED);
        calc.setCalculatedAt(LocalDateTime.now());
        calc.setCreatedAt(LocalDateTime.now());

        salaryCalculationRepository.save(calc);

        CalculationHistory step = new CalculationHistory();
        step.setCalculation(calc);
        step.setStepName("APPLY_TAX_AND_SOCIAL");
        step.setStepOrder(1);
        step.setInputData(toJson(event));
        step.setOutputData(toJson(Map.of(
                "taxRate", taxRate,
                "taxes", taxes,
                "socialContributions", social,
                "netSalary", net
        )));
        step.setCreatedAt(LocalDateTime.now());

        calculationHistoryRepository.save(step);
    }

    private String toJson(Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}

