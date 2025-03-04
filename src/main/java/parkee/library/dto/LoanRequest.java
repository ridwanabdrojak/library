package parkee.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoanRequest {

    @NotBlank(message = "LogsId cannot be blank")
    private String logsId;

    @NotBlank(message = "NIK cannot be blank")
    @Pattern(regexp = "\\d{16}", message = "NIK must be exactly 16 digits and numeric")
    private String nik;

    @NotBlank(message = "ISBN cannot be blank")
    private String isbn;

    @NotBlank(message = "Loan deadline cannot be blank")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Loan deadline must be in format YYYY-MM-DD")
    private String loanDeadline;
}
