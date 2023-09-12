package org.nmng.library.manager.service;

import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.dto.request.LockUserDto;
import org.nmng.library.manager.entity.Role;
import org.nmng.library.manager.entity.User;
import org.nmng.library.manager.exception.InternalServerException;
import org.nmng.library.manager.exception.InvalidRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Transactional
public interface UserService extends UserDetailsService {
    ResponseEntity<?> getAllUsers();

    ResponseEntity<?> getAllUsers(String role);

    ResponseEntity<?> getSpecifiedUser(String identifiable);

    ResponseEntity<?> getSpecifiedUser(String identifiable, Role role);

    ResponseEntity<?> createUser(CreateUserDto dto);

    ResponseEntity<?> createUser(CreateUserDto dto, Role role);

    ResponseEntity<?> deleteUser(String identifiable);

    ResponseEntity<?> deleteUser(String identifiable, Role role);

    default ResponseEntity<?> lockUser(LockUserDto dto, Role role) {
        if (dto == null) throw new InvalidRequestException("Identifiable not found");
        if (role == null) throw new InternalServerException("Role not found");

        return null;
    }

    ResponseEntity<?> changeEncoder(String encoder);

    User findUser(String identifiable);

    User findUser(String identifiable, Role role);

    List<Role> queryRoles(List<String> roleNames);

    Role queryRole(String roleName);
}
