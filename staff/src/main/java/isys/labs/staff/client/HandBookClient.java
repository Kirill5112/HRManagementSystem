package isys.labs.staff.client;

import isys.labs.staff.dto.BenefitCategoryDtoFromHandbook;
import isys.labs.staff.dto.PositionDtoFromHandBook;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class HandBookClient {

    private final RestTemplate restTemplate;
    private final String referenceBaseUrl = "http://localhost:8081";

    public HandBookClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<PositionDtoFromHandBook> getAllPositions() {
        PositionDtoFromHandBook[] response =
                restTemplate.getForObject(referenceBaseUrl + "/api/positions", PositionDtoFromHandBook[].class);
        assert response != null;
        return Arrays.asList(response);
    }

    public PositionDtoFromHandBook getPositionById(Long id) {
        return restTemplate.getForObject(referenceBaseUrl + "/api/positions/{id}",
                PositionDtoFromHandBook.class, id);
    }

    public BenefitCategoryDtoFromHandbook getBenefitCategory(Long benefitCategoryId){
        return restTemplate.getForObject(
                referenceBaseUrl + "/api/benefit-categories/{id}",
                BenefitCategoryDtoFromHandbook.class,
                benefitCategoryId
        );
    }
}
