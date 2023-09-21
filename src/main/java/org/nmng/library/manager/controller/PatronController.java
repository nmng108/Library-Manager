package org.nmng.library.manager.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.nmng.library.manager.dto.request.CreatePatronDto;
import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.dto.request.LockUserDto;
import org.nmng.library.manager.service.PatronService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/patrons", "/api/patrons/"})
@Validated
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
    public Object getById(@PathVariable @NotBlank String identifiable) {
        return this.patronService.getSpecifiedUser(identifiable);
    }

    @PostMapping
    public Object createUser(@RequestBody @Valid CreatePatronDto dto) {
        return this.patronService.createUser(dto);
    }

    @DeleteMapping({"/{identifiable}", "/{identifiable}/"})
    public Object deleteUser(@PathVariable @NotBlank String identifiable) {
        return this.patronService.deleteUser(identifiable);
    }

    @PatchMapping("/lock")
    public ResponseEntity<?> lockPatron(@RequestBody @Valid LockUserDto dto) {
        return this.patronService.lockUser(dto);
    }
}
