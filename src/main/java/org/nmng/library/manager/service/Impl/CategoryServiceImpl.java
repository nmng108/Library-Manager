package org.nmng.library.manager.service.Impl;

import org.nmng.library.manager.dao.CategoryRepository;
import org.nmng.library.manager.dao.BookRepository;
import org.nmng.library.manager.dto.request.CreateBookDto;
import org.nmng.library.manager.entity.Book;
import org.nmng.library.manager.entity.Category;
import org.nmng.library.manager.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(this.categoryRepository.findAll());
    }

    @Override
    public ResponseEntity<?> getSpecifiedOne(String identifiable) {
        Category category = this.findCategory(identifiable);
        return category == null ?
                ResponseEntity.notFound().build()
                :
                ResponseEntity.ok(category);
    }

    @Override
    public ResponseEntity<?> create(String name) {
        if (this.categoryRepository.existsByNameIgnoreCase(name)) {
            return ResponseEntity.badRequest().build();
        }

        Category category = new Category(name);
        category = this.categoryRepository.save(category);

        return  ResponseEntity.ok(category);
    }

    @Override
    public ResponseEntity<?> delete(String identifiable) {
        Category category = this.findCategory(identifiable);

        if (category == null) return ResponseEntity.noContent().build();

        // cannot delete when there are refs to the category
        if (this.bookRepository.existsByCategory(category)) {
            return ResponseEntity.badRequest().body("category is being used");
        }

        this.categoryRepository.delete(category);

        return ResponseEntity.ok(category);
    }

    /**
     *
     * @param identifiable id, name
     * @return
     */
    public Category findCategory(String identifiable) {
        try {
            return this.categoryRepository.findById(Integer.parseInt(identifiable))
                    .orElse(null);
        } catch (NumberFormatException ignored) {}

        return this.categoryRepository.findByNameIgnoreCase(identifiable).orElse(null);
//        throw new RuntimeException("Book not found");
    }
}
