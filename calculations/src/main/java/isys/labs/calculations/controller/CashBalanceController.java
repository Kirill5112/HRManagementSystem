package isys.labs.calculations.controller;

import isys.labs.calculations.dto.CashBalanceDto;
import isys.labs.calculations.service.CashBalanceService;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cash-balances")
public class CashBalanceController {
    private final CashBalanceService service;

    @GetMapping
    public List<CashBalanceDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{departmentId}")
    public CashBalanceDto getCashBalanceByDepartmentId(@PathVariable Long departmentId) {
        return service.getCashBalanceByDepartmentId(departmentId);
    }

    @PostMapping
    public CashBalanceDto createCashBalance(@RequestBody CashBalanceDto dto) {
        return service.createCashBalance(dto);
    }

    @PutMapping("/{id}")
    public CashBalanceDto updateCashBalance(@PathVariable Long id, @RequestBody CashBalanceDto dto) {
        return service.updateCashBalance(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCashBalance(@PathVariable Long id) {
        service.deleteCashBalance(id);
    }
}
