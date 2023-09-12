package org.nmng.library.manager.dao;

import org.nmng.library.manager.entity.Book;
import org.nmng.library.manager.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT EXISTS (SELECT b FROM Book b WHERE b.name = :name " +
            "AND CASE WHEN (:bookNumber IS NULL) THEN b.bookNumber is null ELSE b.bookNumber = :bookNumber END " +
            "AND b.authors = :authors)"
    )
    boolean hasExisted(String name, Integer bookNumber, String authors);

    @Query("SELECT EXISTS (SELECT b FROM Book b WHERE b.category = :category)")
    boolean existsByCategory(Category category);
}
