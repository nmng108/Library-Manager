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

    @GetMapping({"/{identifier}", "/{identifier}/"})
    public Object getById(@PathVariable String identifier) {
        return this.librarianService.getSpecifiedUser(identifier);
    }

    @PostMapping
    public Object createUser(@RequestBody @Valid CreateUserDto dto) {
        return this.librarianService.createUser(dto);
    }

    @DeleteMapping({"/{identifier}", "/{identifier}/"})
    public Object deleteUser(@PathVariable String identifier) {
        return this.librarianService.deleteUser(identifier);
    }
}
