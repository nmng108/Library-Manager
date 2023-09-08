package org.nmng.library.manager.service;

import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.dto.response.UserDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface PatronService {
    static final String ROLE_NAME = "PATRON";

    List<UserDto> getAllUsers();
    Object getSpecifiedUser(String identifier);
    Object createUser(CreateUserDto dto);
    Object deleteUser(String identifier);
}
