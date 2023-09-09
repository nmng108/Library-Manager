package org.nmng.library.manager.service;

import org.nmng.library.manager.dto.request.CreateBookDto;
import org.nmng.library.manager.entity.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface BookService {
    ResponseEntity<?> getAll();

    ResponseEntity<?> getSpecifiedOne(String identifiable);

    ResponseEntity<?> create(CreateBookDto dto);

    ResponseEntity<?> delete(String identifiable);
}
