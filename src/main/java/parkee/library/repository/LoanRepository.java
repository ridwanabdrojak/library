package parkee.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import parkee.library.entity.Loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO loan
        (nik_borrower, isbn_book, loan_date, loan_deadline, created_at, updated_at)
        VALUES (:nik, :isbn, NOW(), :loanDeadline, NOW(), NOW())
        """, nativeQuery = true)
    void insertLoan(@Param("nik") String nik,
                    @Param("isbn") String isbn,
                    @Param("loanDeadline") LocalDate loanDeadline);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE loan
        SET return_date = NOW(),
            status_return = CASE
                WHEN NOW() <= loan_deadline THEN 'Ontime'
                ELSE 'Late'
            END,
            updated_at = NOW()
        WHERE nik_borrower = :nik
        AND isbn_book = :isbn
        """,
            nativeQuery = true)
    void returnLoan(@Param("nik") String nik,
                    @Param("isbn") String isbn);

    @Query(value = """
            SELECT count(*)
            FROM loan
            WHERE nik_borrower = :nik
            AND return_date is null
            """, nativeQuery = true)
    int countActiveLoan(@Param("nik") String nik);

    @Query(value = """
            SELECT count(*)
            FROM loan
            WHERE nik_borrower = :nik
            AND isbn_book = :isbn
            AND return_date is null
            """, nativeQuery = true)
    int loanIsValid(@Param("nik") String nik,
                    @Param("isbn") String isbn);

    @Query(value = """
            SELECT l.nik_borrower, br.name, l.isbn_book, b.title, l.loan_date, l.loan_deadline, l.return_date, l.status_return
            FROM loan l
            LEFT JOIN borrower br ON br.nik = l.nik_borrower
            LEFT JOIN book b ON b.isbn = l.isbn_book
            WHERE return_date is not null
            """, nativeQuery = true)
    List<Object[]> getListLoan();

}
