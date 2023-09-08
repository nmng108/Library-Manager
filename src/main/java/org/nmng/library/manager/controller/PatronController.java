package org.nmng.library.manager.controller;

import jakarta.validation.Valid;
import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.service.LibrarianService;
import org.nmng.library.manager.service.PatronService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/patrons", "/api/patrons/"})
public class PatronController {
    private final PatronService patronService;

    public PatronController(PatronService patronService) {
        this.patronService = patronService;
    }

    @GetMapping
    public Object getAll() {
        return this.patronService.getAllUsers();
    }

    @GetMapping({"/{identifier}", "/{identifier}/"})
    public Object getById(@PathVariable String identifier) {
        return this.patronService.getSpecifiedUser(identifier);
    }

    @PostMapping
    public Object createUser(@RequestBody @Valid CreateUserDto dto) {
        return this.patronService.createUser(dto);
    }

    @DeleteMapping({"/{identifier}", "/{identifier}/"})
    public Object deleteUser(@PathVariable String identifier) {
        return this.patronService.deleteUser(identifier);
    }
}
