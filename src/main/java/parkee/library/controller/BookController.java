package parkee.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import parkee.library.dto.AddBookRequest;
import parkee.library.service.AddBookService;

@RestController
@RequiredArgsConstructor
@Validated
public class BookController {

    private final AddBookService addBookService;

    @PostMapping("/add/book")
    public ResponseEntity<?> addBook(@Valid @RequestBody AddBookRequest requestBody) {
        return addBookService.addBookSvc(requestBody);
    }
}
