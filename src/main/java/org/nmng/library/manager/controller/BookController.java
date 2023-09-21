package org.nmng.library.manager.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.nmng.library.manager.dto.request.BookSearchDto;
import org.nmng.library.manager.dto.request.CreateBookDto;
import org.nmng.library.manager.dto.request.UpdateBookDto;
import org.nmng.library.manager.service.BookService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/books", "/api/books/"})
@Validated
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Object getAll(@Valid BookSearchDto dto) {
        return this.bookService.getAll(dto);
    }

    @GetMapping({"/{identifiable}", "/{identifiable}/"})
    public Object getById(@PathVariable @NotBlank String identifiable) {
        return this.bookService.getSpecifiedOne(identifiable);
    }

    @PostMapping
    public Object create(@RequestBody @Valid CreateBookDto dto) {
        return this.bookService.create(dto);
    }

    @PatchMapping
    public Object update(@RequestBody @Valid UpdateBookDto dto) {
        return this.bookService.update(dto);
    }

    @DeleteMapping({"/{identifiable}", "/{identifiable}/"})
    public Object delete(@PathVariable @NotBlank String identifiable) {
        return this.bookService.delete(identifiable);
    }
}
