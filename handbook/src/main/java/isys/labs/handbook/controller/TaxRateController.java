package isys.labs.handbook.controller;

import isys.labs.handbook.dto.TaxRateDto;
import isys.labs.handbook.service.TaxRateService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tax-rates")
public class TaxRateController {

    private final TaxRateService taxRateService;

    public TaxRateController(TaxRateService taxRateService) {
        this.taxRateService = taxRateService;
    }

    @GetMapping
    public List<TaxRateDto> getAll() {
        return taxRateService.getAll();
    }

    @GetMapping("/{id}")
    public TaxRateDto getById(@PathVariable Long id) {
        return taxRateService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaxRateDto create(@RequestBody TaxRateDto taxRate) {
        return taxRateService.create(taxRate);
    }

    @PutMapping("/{id}")
    public TaxRateDto update(@PathVariable Long id,
                          @RequestBody TaxRateDto taxRate) {
        return taxRateService.update(id, taxRate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taxRateService.delete(id);
    }

    @GetMapping("/effective")
    public Optional<TaxRateDto> getEffective(@RequestParam("date")
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                          LocalDate date) {
        return taxRateService.getEffectiveTaxRate(date);
    }
}
