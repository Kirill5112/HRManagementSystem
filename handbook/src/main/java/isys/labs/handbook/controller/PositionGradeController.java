package isys.labs.handbook.controller;

import isys.labs.handbook.dto.PositionGradeDto;
import isys.labs.handbook.service.PositionGradeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/position-grades")
public class PositionGradeController {

    private final PositionGradeService positionGradeService;

    public PositionGradeController(PositionGradeService positionGradeService) {
        this.positionGradeService = positionGradeService;
    }

    @GetMapping("/by-position/{positionId}")
    public List<PositionGradeDto> getByPosition(@PathVariable Long positionId) {
        return positionGradeService.getByPosition(positionId);
    }

    @GetMapping("/by-grade/{gradeId}")
    public List<PositionGradeDto> getByGrade(@PathVariable Long gradeId) {
        return positionGradeService.getByGrade(gradeId);
    }

    @GetMapping("/by-pos-and-gra/{positionId}/{gradeId}")
    public PositionGradeDto getByPositionAndGrade(@PathVariable Long positionId, @PathVariable Long gradeId){
        return positionGradeService.getByBothId(positionId, gradeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PositionGradeDto assignGrade(@RequestParam Long positionId,
                                     @RequestParam Long gradeId,
                                     @RequestParam BigDecimal multiplier) {
        return positionGradeService.assignGradeToPosition(positionId, gradeId, multiplier);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        positionGradeService.removeGradeFromPosition(id);
    }
}
