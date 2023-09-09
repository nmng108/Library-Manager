package org.nmng.library.manager.service;

import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.dto.response.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface LibrarianService {
    static final String ROLE_NAME = "LIBRARIAN";

    ResponseEntity<?> getAllUsers();

    ResponseEntity<?> getSpecifiedUser(String identifiable);

    ResponseEntity<?> createUser(CreateUserDto dto);

    ResponseEntity<?> deleteUser(String identifiable);
}
