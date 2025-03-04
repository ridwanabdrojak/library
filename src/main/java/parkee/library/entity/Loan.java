package parkee.library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "borrower")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nikBorrower;

    private String isbnBook;

    private LocalDate loanDate;

    private LocalDate loanDeadline;

    private LocalDate returnDate;

    private String statusReturn;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}