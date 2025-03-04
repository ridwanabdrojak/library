package parkee.library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "borrower")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Borrower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nik;

    private String name;

    private int email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}