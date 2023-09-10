package org.nmng.library.manager.controller;

import jakarta.validation.Valid;
import org.nmng.library.manager.dto.request.CreateUserDto;
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

    @GetMapping({"/{identifiable}", "/{identifiable}/"})
    public Object getById(@PathVariable String identifiable) {
        return this.patronService.getSpecifiedUser(identifiable);
    }

    @PostMapping
    public Object createUser(@RequestBody CreateUserDto dto) {
        return this.patronService.createUser(dto);
    }

    @DeleteMapping({"/{identifiable}", "/{identifiable}/"})
    public Object deleteUser(@PathVariable String identifiable) {
        return this.patronService.deleteUser(identifiable);
    }
}
