package parkee.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import parkee.library.dto.ListLoanRequest;
import parkee.library.dto.LoanRequest;
import parkee.library.dto.ReturnRequest;
import parkee.library.service.LoanService;

@RestController
@RequiredArgsConstructor
@Validated
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/loan")
    public ResponseEntity<?> loan(@Valid @RequestBody LoanRequest requestBody) {
        return loanService.loanSvc(requestBody);
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnLoan(@Valid @RequestBody ReturnRequest requestBody) {
        return loanService.returnSvc(requestBody);
    }

    @PostMapping("/get/list/loan")
    public ResponseEntity<?> getListLoan(@Valid @RequestBody ListLoanRequest requestBody) {
        return loanService.listLoanSvc(requestBody);
    }
}
