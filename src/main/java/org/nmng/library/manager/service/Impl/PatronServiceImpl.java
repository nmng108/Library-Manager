package org.nmng.library.manager.service.Impl;

import org.nmng.library.manager.dao.UserRepository;
import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.dto.response.UserDto;
import org.nmng.library.manager.entity.Role;
import org.nmng.library.manager.service.LibrarianService;
import org.nmng.library.manager.service.PatronService;
import org.nmng.library.manager.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatronServiceImpl implements PatronService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final Role ROLE;

    public PatronServiceImpl(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.ROLE = userService.queryRole(ROLE_NAME);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return this.userService.getAllUsers(ROLE_NAME);
    }

    @Override
    public Object getSpecifiedUser(String identifier) {
        return this.userService.getSpecifiedUser(identifier, this.ROLE);
    }

    @Override
    public Object createUser(CreateUserDto dto) {
        return this.userService.createUser(dto, this.ROLE);
    }

    @Override
    public Object deleteUser(String identifier) {
        return this.userService.deleteUser(identifier, this.ROLE);
    }
}
