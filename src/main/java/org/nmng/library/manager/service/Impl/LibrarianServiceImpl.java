package org.nmng.library.manager.service.Impl;

import org.nmng.library.manager.dao.UserRepository;
import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.dto.response.UserDto;
import org.nmng.library.manager.entity.Role;
import org.nmng.library.manager.service.LibrarianService;
import org.nmng.library.manager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibrarianServiceImpl implements LibrarianService {
    private final UserService userService;
    private final Role ROLE;

    public LibrarianServiceImpl(UserService userService) {
        this.userService = userService;
        this.ROLE = userService.queryRole(ROLE_NAME);
    }

    @Override
    public ResponseEntity<?> getAllUsers() {
        return this.userService.getAllUsers(ROLE_NAME);
    }

    @Override
    public ResponseEntity<?> getSpecifiedUser(String identifiable) {
        return this.userService.getSpecifiedUser(identifiable, this.ROLE);
    }

    @Override
    public ResponseEntity<?> createUser(CreateUserDto dto) {
        return this.userService.createUser(dto, this.ROLE);
    }

    @Override
    public ResponseEntity<?> deleteUser(String identifiable) {
        return this.userService.deleteUser(identifiable, this.ROLE);
    }
}
