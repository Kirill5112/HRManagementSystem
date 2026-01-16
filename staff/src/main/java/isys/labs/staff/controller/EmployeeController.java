package isys.labs.staff.controller;

import isys.labs.staff.dto.EmployeeDto;
import isys.labs.staff.dto.SalaryCalculationRequest;
import isys.labs.staff.event.SalaryCalculationEvent;
import isys.labs.staff.service.EmployeeService;
import isys.labs.staff.service.SalaryCalculationProducer;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final SalaryCalculationProducer salaryCalculationProducer;

    public EmployeeController(EmployeeService employeeService, SalaryCalculationProducer salaryCalculationProducer) {
        this.employeeService = employeeService;
        this.salaryCalculationProducer = salaryCalculationProducer;
    }

    @GetMapping
    public Page<EmployeeDto> getEmployees(Pageable pageable) {
        return employeeService.getEmployees(pageable);
    }

    @GetMapping("/{id}")
    public EmployeeDto getById(@PathVariable Long id) {
        return employeeService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDto create(@RequestBody @Valid EmployeeDto dto) {
        return employeeService.create(dto);
    }

    @PostMapping("/{id}/calculate-salary")
    public ResponseEntity<Void> calculateSalary(
            @PathVariable Long id,
            @RequestBody SalaryCalculationRequest request
    ) {
        SalaryCalculationEvent event = new SalaryCalculationEvent();
        event.setEmployeeId(id);
        event.setPeriodStart(request.getPeriodStart());
        event.setPeriodEnd(request.getPeriodEnd());
        event.setGrossSalary(request.getGrossSalary());
        event.setBonuses(request.getBonuses());
        event.setDeductions(request.getDeductions());

        salaryCalculationProducer.sendSalaryCalculation(event);

        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{id}")
    public EmployeeDto update(@PathVariable Long id,
                              @RequestBody @Valid EmployeeDto dto) {
        return employeeService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        employeeService.delete(id);
    }
}
