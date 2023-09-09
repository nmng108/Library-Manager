package org.nmng.library.manager.controller;

import jakarta.validation.Valid;
import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/users", "/api/users/"})
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Object getAll() {
        return this.userService.getAllUsers();
    }

    @GetMapping({"/{identifiable}", "/{identifiable}/"})
    public Object getById(@PathVariable String identifiable) {
        return this.userService.getSpecifiedUser(identifiable);
    }

    @PostMapping
    public Object createUser(@RequestBody @Valid CreateUserDto dto) {
        return this.userService.createUser(dto);
    }

    @DeleteMapping({"/{identifiable}", "/{identifiable}/"})
    public Object deleteUser(@PathVariable String identifiable) {
        return this.userService.deleteUser(identifiable);
    }
}
