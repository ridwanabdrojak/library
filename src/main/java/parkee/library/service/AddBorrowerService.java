package parkee.library.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import parkee.library.dto.AddBorrowerRequest;
import parkee.library.dto.LibraryResponse;
import parkee.library.repository.BorrowerRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddBorrowerService {

    private final BorrowerRepository borrowerRepository;

    public ResponseEntity<?> addBorrowerSvc(AddBorrowerRequest requestBody) {
        String logsId = requestBody.getLogsId();
        LibraryResponse responseMap = new LibraryResponse();
        responseMap.setLogsId(logsId);

        Gson gson = new Gson();

        try {
            log.info("{} - start to add borrower. request: {}", logsId, gson.toJson(requestBody));

            String nik = requestBody.getNik();
            String name = requestBody.getName();
            String email = requestBody.getEmail();

            // check borrowed is registered
            int isRegistered = borrowerRepository.selectCountByNikAndEmail(nik, email);
            if (isRegistered > 0) {
                log.info("{} - nik or email is registered", logsId);
                responseMap.setStatus("failed");
                responseMap.setMessage("nik or email is registered");
                log.info("{} - End-to-End processing add borrower. Response: {}", logsId, gson.toJson(responseMap));
                return ResponseEntity.status(HttpStatus.OK).body(responseMap);
            } else {
                // insert borrower to db
                borrowerRepository.insertBorrower(nik, name, email);
            }

            responseMap.setStatus("success");
            responseMap.setMessage("Add borrower successfully");
            log.info("{} - End-to-End processing add borrower. Response: {}", logsId, gson.toJson(responseMap));
            return ResponseEntity.status(HttpStatus.OK).body(responseMap);

        } catch (Exception e) {
            log.error("{} - Unexpected error while adding borrower: {}", logsId, e.getMessage(), e);
            responseMap.setStatus("failed");
            responseMap.setMessage("Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
    }

}
