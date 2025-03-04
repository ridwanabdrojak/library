package parkee.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import parkee.library.dto.AddBorrowerRequest;
import parkee.library.service.AddBorrowerService;

@RestController
@RequiredArgsConstructor
@Validated
public class BorrowerController {

    private final AddBorrowerService addBorrowerService;

    @PostMapping("/add/borrower")
    public ResponseEntity<?> addBorrower(@Valid @RequestBody AddBorrowerRequest requestBody) {
        return addBorrowerService.addBorrowerSvc(requestBody);
    }
}
