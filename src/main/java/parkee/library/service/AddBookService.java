package parkee.library.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import parkee.library.dto.AddBookRequest;
import parkee.library.dto.LibraryResponse;
import parkee.library.dto.database.BookDto;
import parkee.library.repository.BookRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddBookService {

    private final BookRepository bookRepository;

    public ResponseEntity<?> addBookSvc(AddBookRequest requestBody) {
        String logsId = requestBody.getLogsId();
        LibraryResponse responseMap = new LibraryResponse();
        responseMap.setLogsId(logsId);

        Gson gson = new Gson();

        try {
            log.info("{} - start to add book. request: {}", logsId, gson.toJson(requestBody));

            String title = requestBody.getTitle();
            String isbn = requestBody.getIsbn();
            int stock = Integer.parseInt(requestBody.getStock());

            // check book where isbn
            BookDto book = bookRepository.selectBookByIsbn(isbn);
            if (book == null) {
                // add book to db
                bookRepository.insertBook(title, isbn, stock);
            } else {
                // update stock
                stock += book.getStock();
                // check title
                if (!title.equalsIgnoreCase(book.getTitle())) {
                    log.info("{} - ISBN is already exist", logsId);
                    responseMap.setStatus("failed");
                    responseMap.setMessage("Add book failed, ISBN is already exist");
                    log.info("{} - End-to-End processing add book. Response: {}", logsId, gson.toJson(responseMap));
                    return ResponseEntity.status(HttpStatus.OK).body(responseMap);
                } else {
                    // update stock
                    bookRepository.updateStock(stock, isbn, title);
                }
            }

            responseMap.setStatus("success");
            responseMap.setMessage("Add book successfully");
            log.info("{} - End-to-End processing add book. Response: {}", logsId, gson.toJson(responseMap));
            return ResponseEntity.status(HttpStatus.OK).body(responseMap);

        } catch (Exception e) {
            log.error("{} - Unexpected error while adding book: {}", logsId, e.getMessage(), e);
            responseMap.setStatus("failed");
            responseMap.setMessage("Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }

}
