package parkee.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LibraryResponse {

    private String logsId;
    private String status = "success";
    private String message;
    private Object data = new ArrayList<>();
}
