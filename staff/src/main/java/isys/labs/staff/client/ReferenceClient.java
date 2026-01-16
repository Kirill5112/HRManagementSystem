package isys.labs.staff.client;

import isys.labs.staff.dto.PositionDtoFromHandBook;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class ReferenceClient {

    private final RestTemplate restTemplate;

    public ReferenceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<PositionDtoFromHandBook> getAllPositions() {
        PositionDtoFromHandBook[] response =
                restTemplate.getForObject("http://localhost:8081/api/positions", PositionDtoFromHandBook[].class);
        assert response != null;
        return Arrays.asList(response);
    }

    public PositionDtoFromHandBook getPositionById(Long id) {
        return restTemplate.getForObject("http://localhost:8081/api/positions/{id}",
                PositionDtoFromHandBook.class, id);
    }
}
