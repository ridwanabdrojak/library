package parkee.library.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import parkee.library.dto.LibraryResponse;
import parkee.library.dto.ListLoanRequest;
import parkee.library.dto.LoanRequest;
import parkee.library.dto.ReturnRequest;
import parkee.library.dto.database.BookDto;
import parkee.library.repository.BookRepository;
import parkee.library.repository.BorrowerRepository;
import parkee.library.repository.LoanRepository;
import parkee.library.util.LoanUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanService {

    private final BorrowerRepository borrowerRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final LoanUtil loanUtil;

    public ResponseEntity<?> loanSvc(LoanRequest requestBody) {
        String logsId = requestBody.getLogsId();
        LibraryResponse responseMap = new LibraryResponse();
        responseMap.setLogsId(logsId);

        Gson gson = new Gson();

        try {
            log.info("{} - start to loan. request: {}", logsId, gson.toJson(requestBody));

            String nik = requestBody.getNik();
            String isbn = requestBody.getIsbn();
            String deadlineString = requestBody.getLoanDeadline();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate deadlineDate = LocalDate.parse(deadlineString, formatter);

            // check borrowed is registered
            int isRegistered = borrowerRepository.selectCountByNik(nik);
            if (isRegistered == 0) {
                log.info("{} - nik is not registered", logsId);
                responseMap.setStatus("failed");
                responseMap.setMessage("nik is not registered");
                log.info("{} - End-to-End processing loan. Response: {}", logsId, gson.toJson(responseMap));
                return ResponseEntity.status(HttpStatus.OK).body(responseMap);
            }

            // check book stock
            BookDto book = bookRepository.selectBookByIsbn(isbn);
            if (book == null || book.getStock() == 0) {
                log.info("{} - book stock is empty", logsId);
                responseMap.setStatus("failed");
                responseMap.setMessage("book stock is empty");
                log.info("{} - End-to-End processing loan. Response: {}", logsId, gson.toJson(responseMap));
                return ResponseEntity.status(HttpStatus.OK).body(responseMap);
            }

            // check deadline is valid
            if (!loanUtil.isLoanDeadlineValid(deadlineDate)) {
                log.info("{} - deadline is not valid", logsId);
                responseMap.setStatus("failed");
                responseMap.setMessage("deadline is not valid");
                log.info("{} - End-to-End processing loan. Response: {}", logsId, gson.toJson(responseMap));
                return ResponseEntity.status(HttpStatus.OK).body(responseMap);
            }

            // check is borrowing another book
            int isLoan = loanRepository.countActiveLoan(nik);
            if (isLoan > 0) {
                log.info("{} - borrowing another book", logsId);
                responseMap.setStatus("failed");
                responseMap.setMessage("borrowing another book");
                log.info("{} - End-to-End processing loan. Response: {}", logsId, gson.toJson(responseMap));
                return ResponseEntity.status(HttpStatus.OK).body(responseMap);
            }

            loanRepository.insertLoan(nik, isbn, deadlineDate);
            bookRepository.minStockBook(isbn);

            responseMap.setStatus("success");
            responseMap.setMessage("Loan successfully");
            log.info("{} - End-to-End processing loan. Response: {}", logsId, gson.toJson(responseMap));
            return ResponseEntity.status(HttpStatus.OK).body(responseMap);

        } catch (Exception e) {
            log.error("{} - Unexpected error while adding loan: {}", logsId, e.getMessage(), e);
            responseMap.setStatus("failed");
            responseMap.setMessage("Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }

    public ResponseEntity<?> returnSvc(ReturnRequest requestBody) {
        String logsId = requestBody.getLogsId();
        LibraryResponse responseMap = new LibraryResponse();
        responseMap.setLogsId(logsId);

        Gson gson = new Gson();

        try {
            log.info("{} - start to return. request: {}", logsId, gson.toJson(requestBody));

            String nik = requestBody.getNik();
            String isbn = requestBody.getIsbn();

            // check is borrowing another book
            int isLoan = loanRepository.loanIsValid(nik, isbn);
            if (isLoan == 0) {
                log.info("{} - loan is not valid", logsId);
                responseMap.setStatus("failed");
                responseMap.setMessage("loan is not valid");
                log.info("{} - End-to-End processing return. Response: {}", logsId, gson.toJson(responseMap));
                return ResponseEntity.status(HttpStatus.OK).body(responseMap);
            }

            loanRepository.returnLoan(nik, isbn);
            bookRepository.plusStockBook(isbn);

            responseMap.setStatus("success");
            responseMap.setMessage("Return successfully");
            log.info("{} - End-to-End processing Return. Response: {}", logsId, gson.toJson(responseMap));
            return ResponseEntity.status(HttpStatus.OK).body(responseMap);

        } catch (Exception e) {
            log.error("{} - Unexpected error while return loan: {}", logsId, e.getMessage(), e);
            responseMap.setStatus("failed");
            responseMap.setMessage("Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }

    public ResponseEntity<?> listLoanSvc(ListLoanRequest requestBody) {
        String logsId = requestBody.getLogsId();
        LibraryResponse responseMap = new LibraryResponse();
        responseMap.setLogsId(logsId);

        Gson gson = new Gson();

        try {
            log.info("{} - start to get list loan. request: {}", logsId, gson.toJson(requestBody));

            responseMap.setStatus("success");
            responseMap.setMessage("Get list Loan successfully");
            responseMap.setData(loanUtil.getLoanList());
            log.info("{} - End-to-End processing get list loan. Response: {}", logsId, gson.toJson(responseMap));
            return ResponseEntity.status(HttpStatus.OK).body(responseMap);

        } catch (Exception e) {
            log.error("{} - Unexpected error while get list loan: {}", logsId, e.getMessage(), e);
            responseMap.setStatus("failed");
            responseMap.setMessage("Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }

}
