package parkee.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class AddBorrowerRequest {

    @NotBlank(message = "LogsId cannot be blank")
    private String logsId;

    @NotBlank(message = "NIK cannot be blank")
    @Pattern(regexp = "\\d{16}", message = "NIK must be exactly 16 digits and numeric")
    private String nik;

    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only letters and spaces")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private String email;
}
