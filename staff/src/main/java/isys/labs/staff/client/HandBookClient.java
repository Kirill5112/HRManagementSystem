package isys.labs.staff.client;

import isys.labs.staff.dto.BenefitCategoryFromHandbookDto;
import isys.labs.staff.dto.PositionFromHandBookDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class HandBookClient {

    private final RestTemplate restTemplate;

    @Value("${server.application.handbookUrl}")
    private String referenceBaseUrl;

    public HandBookClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<PositionFromHandBookDto> getAllPositions() {
        PositionFromHandBookDto[] response =
                restTemplate.getForObject(referenceBaseUrl + "/api/positions", PositionFromHandBookDto[].class);
        if(response == null)
            throw new IllegalStateException("responce null");
        return Arrays.asList(response);
    }

    public PositionFromHandBookDto getPositionById(Long id) {
        return restTemplate.getForObject(referenceBaseUrl + "/api/positions/{id}",
                PositionFromHandBookDto.class, id);
    }

    public BenefitCategoryFromHandbookDto getBenefitCategory(Long benefitCategoryId){
        return restTemplate.getForObject(
                referenceBaseUrl + "/api/benefit-categories/{id}",
                BenefitCategoryFromHandbookDto.class,
                benefitCategoryId
        );
    }
}
