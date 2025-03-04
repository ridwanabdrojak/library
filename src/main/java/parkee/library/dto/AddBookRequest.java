package parkee.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class AddBookRequest {

    @NotBlank(message = "LogsId cannot be blank")
    private String logsId;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "ISBN cannot be blank")
    private String isbn;

    @NotBlank(message = "Stock cannot be blank")
    @Pattern(regexp = "\\d+", message = "Stock must be numeric")
    private String stock;
}
