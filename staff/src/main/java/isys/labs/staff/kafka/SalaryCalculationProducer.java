package isys.labs.staff.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalaryCalculationProducer {

    private final KafkaTemplate<String, SalaryCalculationEvent> kafkaTemplate;

    private static final String TOPIC = "salary-calculation-topic";

    public void sendSalaryCalculation(SalaryCalculationEvent event) {
        kafkaTemplate.send(TOPIC, event.getEmployeeId().toString(), event);
    }
}

