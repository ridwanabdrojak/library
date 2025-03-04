package parkee.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ListLoanRequest {

    @NotBlank(message = "LogsId cannot be blank")
    private String logsId;
}
