package parkee.library.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import parkee.library.repository.LoanRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoanUtil {

    private final LoanRepository loanRepository;

    public boolean isLoanDeadlineValid(LocalDate deadlineDate) {
        LocalDate today = LocalDate.now();

        long daysBetween = ChronoUnit.DAYS.between(today, deadlineDate);

        return daysBetween <= 30;
    }

    public List<LinkedHashMap<String, Object>> getLoanList() {
        List<Object[]> results = loanRepository.getListLoan();
        List<LinkedHashMap<String, Object>> loanList = new ArrayList<>();

        for (Object[] row : results) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("nik", row[0]);
            map.put("name", row[1]);
            map.put("isbnBook", row[2]);
            map.put("title", row[3]);
            map.put("loanDate", row[4]);
            map.put("loanDeadline", row[5]);
            map.put("returnDate", row[6]);
            map.put("statusReturn", row[7]);

            loanList.add(map);
        }
        return loanList;
    }
}
