package org.nmng.library.manager.service;

import org.nmng.library.manager.dto.request.CreateBookDto;
import org.nmng.library.manager.entity.Book;
import org.nmng.library.manager.entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CategoryService {
    ResponseEntity<?> getAll();

    ResponseEntity<?> getSpecifiedOne(String identifiable);

    ResponseEntity<?> create(String dto);

    ResponseEntity<?> delete(String identifiable);
}
