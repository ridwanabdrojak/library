package parkee.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import parkee.library.dto.database.BookDto;
import parkee.library.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO book
            (title, isbn, stock, created_at, updated_at)
            VALUES (:title, :isbn, :stock, NOW(), NOW())
            """, nativeQuery = true)
    void insertBook(@Param("title") String title,
                    @Param("isbn") String isbn,
                    @Param("stock") int stock);

    @Query(value = """
            SELECT title, stock
            FROM book
            WHERE isbn = :isbn
            """, nativeQuery = true)
    BookDto selectBookByIsbn(@Param("isbn") String isbn);

    @Modifying
    @Transactional
    @Query(value = """
            UPDATE book
            SET stock = :stock,
            updated_at = NOW()
            WHERE isbn = :isbn
            AND title = :title
            """,
            nativeQuery = true)
    void updateStock(@Param("stock") int stock,
                     @Param("isbn") String isbn,
                     @Param("title") String title);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE book
        SET stock = CASE WHEN stock > 0 THEN stock - 1 ELSE stock END,
            updated_at = NOW()
        WHERE isbn = :isbn
        """, nativeQuery = true)
    void minStockBook(@Param("isbn") String isbn);

    @Modifying
    @Transactional
    @Query(value = """
        UPDATE book
        SET stock = stock + 1,
            updated_at = NOW()
        WHERE isbn = :isbn
        """, nativeQuery = true)
    void plusStockBook(@Param("isbn") String isbn);

}
