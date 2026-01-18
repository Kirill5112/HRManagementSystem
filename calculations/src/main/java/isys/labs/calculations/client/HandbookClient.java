package isys.labs.calculations.client;

import isys.labs.calculations.dto.BenefitCategoryInfoDto;
import isys.labs.calculations.dto.GradeInfoDto;
import isys.labs.calculations.dto.PositionGradeInfoDto;
import isys.labs.calculations.dto.TaxRateInfoDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Component
public class HandbookClient {

    private final RestTemplate restTemplate;

    private final String referenceBaseUrl = "http://localhost:8081";

    public HandbookClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GradeInfoDto getGrade(Long gradeId) {
        return restTemplate.getForObject(
                referenceBaseUrl + "/api/grades/{id}",
                GradeInfoDto.class,
                gradeId
        );
    }

    public PositionGradeInfoDto getPositionGrade(Long positionId, Long gradeId){
        return restTemplate.getForObject(
                referenceBaseUrl + "/api/position-grades/by-pos-and-gra/{positionId}/{gradeId}",
                PositionGradeInfoDto.class,
                positionId,
                gradeId
        );
    }

    public TaxRateInfoDto getTaxRate(LocalDate date){
        return restTemplate.getForObject(
                referenceBaseUrl + "/api/tax-rates/effective?date={date}",
                TaxRateInfoDto.class,
                date

        );
    }

    public BenefitCategoryInfoDto getBenefitCategory(Long benefitCategoryId){
        return restTemplate.getForObject(
                referenceBaseUrl + "/api/benefit-categories/{id}",
                BenefitCategoryInfoDto.class,
                benefitCategoryId
        );
    }

}
