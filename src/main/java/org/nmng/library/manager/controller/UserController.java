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

    @GetMapping({"/{identifier}", "/{identifier}/"})
    public Object getById(@PathVariable String identifier) {
        return this.userService.getSpecifiedUser(identifier);
    }

    @PostMapping
    public Object createUser(@RequestBody @Valid CreateUserDto dto) {
        return this.userService.createUser(dto);
    }

    @DeleteMapping({"/{identifier}", "/{identifier}/"})
    public Object deleteUser(@PathVariable String identifier) {
        return this.userService.deleteUser(identifier);
    }
}
