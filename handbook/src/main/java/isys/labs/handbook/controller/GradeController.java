package isys.labs.handbook.controller;

import isys.labs.handbook.dto.GradeDto;
import isys.labs.handbook.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping
    public List<GradeDto> getAll() {
        return gradeService.getAll();
    }

    @GetMapping("/{id}")
    public GradeDto getById(@PathVariable Long id) {
        return gradeService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GradeDto create(@RequestBody @Valid GradeDto grade) {
        return gradeService.create(grade);
    }

    @PutMapping("/{id}")
    public GradeDto update(@PathVariable Long id,
                        @RequestBody @Valid GradeDto grade) {
        return gradeService.update(id, grade);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        gradeService.delete(id);
    }
}
