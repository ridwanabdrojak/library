package parkee.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import parkee.library.entity.Borrower;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO borrower
            (nik, name, email, created_at, updated_at)
            VALUES (:nik, :name, :email, NOW(), NOW())
            """, nativeQuery = true)
    void insertBorrower(@Param("nik") String nik,
                    @Param("name") String name,
                    @Param("email") String email);

    @Query(value = """
            SELECT count(*)
            FROM borrower
            WHERE nik = :nik
            OR email = :email
            """, nativeQuery = true)
    int selectCountByNikAndEmail(@Param("nik") String nik,
                                 @Param("email") String email);

    @Query(value = """
            SELECT count(*)
            FROM borrower
            WHERE nik = :nik
            """, nativeQuery = true)
    int selectCountByNik(@Param("nik") String nik);
}
