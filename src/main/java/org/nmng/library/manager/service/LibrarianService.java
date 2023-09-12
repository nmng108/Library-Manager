package org.nmng.library.manager.service;

import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.dto.request.LockUserDto;
import org.nmng.library.manager.dto.response.UserDto;
import org.nmng.library.manager.entity.Role;
import org.nmng.library.manager.exception.InvalidRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface LibrarianService {
    static final String ROLE_NAME = Role.LIBRARIAN;

    ResponseEntity<?> getAllUsers();

    ResponseEntity<?> getSpecifiedUser(String identifiable);

    ResponseEntity<?> createUser(CreateUserDto dto);

    ResponseEntity<?> deleteUser(String identifiable);

    default ResponseEntity<?> lockUser(LockUserDto dto) {
        if (dto == null) throw new InvalidRequestException("Identifiable not found");
        return null;
    }
}
