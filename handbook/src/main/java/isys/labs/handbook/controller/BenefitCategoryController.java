package isys.labs.handbook.controller;

import isys.labs.handbook.dto.BenefitCategoryDto;
import isys.labs.handbook.service.BenefitCategoryService;
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
@RequestMapping("/api/benefit-categories")
public class BenefitCategoryController {

    private final BenefitCategoryService benefitCategoryService;

    public BenefitCategoryController(BenefitCategoryService benefitCategoryService) {
        this.benefitCategoryService = benefitCategoryService;
    }

    @GetMapping
    public List<BenefitCategoryDto> getAll() {
        return benefitCategoryService.getAll();
    }

    @GetMapping("/{id}")
    public BenefitCategoryDto getById(@PathVariable Long id) {
        return benefitCategoryService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BenefitCategoryDto create(@RequestBody @Valid BenefitCategoryDto category) {
        return benefitCategoryService.create(category);
    }

    @PutMapping("/{id}")
    public BenefitCategoryDto update(@PathVariable Long id,
                                  @RequestBody @Valid BenefitCategoryDto category) {
        return benefitCategoryService.update(id, category);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        benefitCategoryService.delete(id);
    }
}
