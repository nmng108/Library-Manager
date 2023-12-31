package org.nmng.library.manager.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.nmng.library.manager.dto.request.CreateLibrarianDto;
import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.dto.request.LockUserDto;
import org.nmng.library.manager.service.LibrarianService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/librarians", "/api/librarians/"})
@Validated
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
    public Object getById(@PathVariable @NotBlank String identifiable) {
        return this.librarianService.getSpecifiedUser(identifiable);
    }

    @PostMapping
    public Object createUser(@RequestBody @Valid CreateLibrarianDto dto) {
        return this.librarianService.createUser(dto);
    }

    @DeleteMapping({"/{identifiable}", "/{identifiable}/"})
    public Object deleteUser(@PathVariable @NotBlank String identifiable) {
        return this.librarianService.deleteUser(identifiable);
    }

    @PatchMapping("/lock")
    public ResponseEntity<?> lockPatron(@RequestBody @Valid LockUserDto dto) {
        return this.librarianService.lockUser(dto);
    }
}
