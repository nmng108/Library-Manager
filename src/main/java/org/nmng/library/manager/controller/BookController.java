package org.nmng.library.manager.controller;

import jakarta.validation.Valid;
import org.nmng.library.manager.dto.request.CreateBookDto;
import org.nmng.library.manager.service.BookService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/books", "/api/books/"})
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Object getAll() {
        return this.bookService.getAll();
    }

    @GetMapping({"/{identifiable}", "/{identifiable}/"})
    public Object getById(@PathVariable String identifiable) {
        return this.bookService.getSpecifiedOne(identifiable);
    }

    @PostMapping
    public Object create(@RequestBody @Valid CreateBookDto dto) {
        return this.bookService.create(dto);
    }

    @DeleteMapping({"/{identifiable}", "/{identifiable}/"})
    public Object delete(@PathVariable String identifiable) {
        return this.bookService.delete(identifiable);
    }
}
