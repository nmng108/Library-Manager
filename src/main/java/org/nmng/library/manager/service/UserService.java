package org.nmng.library.manager.service;

import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.dto.response.UserDto;
import org.nmng.library.manager.entity.Role;
import org.nmng.library.manager.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserService {
    ResponseEntity<?>getAllUsers();

    ResponseEntity<?>getAllUsers(String role);

    ResponseEntity<?>getSpecifiedUser(String identifiable);

    ResponseEntity<?>getSpecifiedUser(String identifiable, Role role);

    ResponseEntity<?>createUser(CreateUserDto dto);

    ResponseEntity<?>createUser(CreateUserDto dto, Role role);

    ResponseEntity<?>deleteUser(String identifiable);

    ResponseEntity<?>deleteUser(String identifiable, Role role);


    User findUser(String identifiable);

    User findUser(String identifiable, Role role);

    List<Role> queryRoles(List<String> roleNames);

    Role queryRole(String roleName);
}
