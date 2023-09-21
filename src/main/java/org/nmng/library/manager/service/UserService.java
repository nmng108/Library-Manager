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

    ResponseEntity<?> getSpecifiedUser(String identifiable, Role serviceRole);

    ResponseEntity<?> createUser(CreateUserDto dto);

    ResponseEntity<?> createUser(User user, Role serviceRole);

    ResponseEntity<?> deleteUser(String identifiable);

    ResponseEntity<?> deleteUser(String identifiable, Role serviceRole);

    /**
     * Lock the user that has serviceRole
     * @param dto LockUserDto
     * @param serviceRole role corresponding to API. E.g. /api/librarians -> serviceRole = LIBRARIAN
     * @return ResponseEntity
     */
    default ResponseEntity<?> lockUser(LockUserDto dto, Role serviceRole) {
        if (dto == null) throw new InvalidRequestException("Identifiable not found");
        if (serviceRole == null) throw new InternalServerException("Role not found");

        return null;
    }

    ResponseEntity<?> changeEncoder(String encoder);

    User findUser(String identifiable);

    User findUser(String identifiable, Role serviceRole);

    List<Role> queryRoles(List<String> roleNames);

    Role queryRole(String roleName);
}
