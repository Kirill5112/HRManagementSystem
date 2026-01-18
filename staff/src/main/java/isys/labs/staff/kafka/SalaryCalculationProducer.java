package isys.labs.staff.kafka;

import isys.labs.staff.dto.SalaryCalculationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalaryCalculationProducer {

    private static final String TOPIC = "salary-calculation-topic";

    private final KafkaTemplate<String, SalaryCalculationEvent> kafkaTemplate;

    public void sendSalaryCalculation(SalaryCalculationEvent event) {
        kafkaTemplate.send(TOPIC, event.getEmployeeId().toString(), event);
    }
}

