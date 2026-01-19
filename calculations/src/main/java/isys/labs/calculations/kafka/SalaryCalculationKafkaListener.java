package isys.labs.calculations.kafka;

import isys.labs.calculations.dto.SalaryCalculationEvent;
import isys.labs.calculations.service.SalaryCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalaryCalculationKafkaListener {

    private final SalaryCalculationService salaryCalculationService;

    @KafkaListener(
            topics = "salary-calculation-topic",
            groupId = "salary-calculation-group"
    )
    public void listen(SalaryCalculationEvent event) {
        salaryCalculationService.handleCalculationEvent(event);
    }
}
