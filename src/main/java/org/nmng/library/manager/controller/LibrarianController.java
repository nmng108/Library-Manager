package org.nmng.library.manager.controller;

import jakarta.validation.Valid;
import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.service.LibrarianService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/librarians", "/api/librarians/"})
public class LibrarianController {
    private final LibrarianService librarianService;

    public LibrarianController(LibrarianService librarianService) {
        this.librarianService = librarianService;
    }

    @GetMapping
    public Object getAll() {
        return this.librarianService.getAllUsers();
    }

    @GetMapping({"/{identifiable}", "/{identifiable}/"})
    public Object getById(@PathVariable String identifiable) {
        return this.librarianService.getSpecifiedUser(identifiable);
    }

    @PostMapping
    public Object createUser(@RequestBody @Valid CreateUserDto dto) {
        return this.librarianService.createUser(dto);
    }

    @DeleteMapping({"/{identifiable}", "/{identifiable}/"})
    public Object deleteUser(@PathVariable String identifiable) {
        return this.librarianService.deleteUser(identifiable);
    }
}
