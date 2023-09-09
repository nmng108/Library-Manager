package org.nmng.library.manager.controller;

import jakarta.validation.Valid;
import org.nmng.library.manager.service.BookService;
import org.nmng.library.manager.service.CategoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/categories", "/api/categories/"})
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Object getAll() {
        return this.categoryService.getAll();
    }

    @GetMapping({"/{identifiable}", "/{identifiable}/"})
    public Object getById(@PathVariable String identifiable) {
        return this.categoryService.getSpecifiedOne(identifiable);
    }

    @PostMapping({"/{name}", "/{name}/"})
    public Object create(@PathVariable @Valid String name) {
        return this.categoryService.create(name);
    }

    @DeleteMapping({"/{identifiable}", "/{identifiable}/"})
    public Object delete(@PathVariable String identifiable) {
        return this.categoryService.delete(identifiable);
    }
}
