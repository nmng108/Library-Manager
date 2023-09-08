package org.nmng.library.manager.service;

import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.dto.response.UserDto;
import org.nmng.library.manager.entity.Role;
import org.nmng.library.manager.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserService {
    List<UserDto> getAllUsers();
    List<UserDto> getAllUsers(String role);

    Object getSpecifiedUser(String identifier);
    Object getSpecifiedUser(String identifier, Role role);

    Object createUser(CreateUserDto dto);
    Object createUser(CreateUserDto dto, Role role);

    Object deleteUser(String identifier);
    Object deleteUser(String identifier, Role role);


    User findUser(String identifier);
    User findUser(String identifier, Role role);

    List<Role> queryRoles(List<String> roleNames);
    Role queryRole(String roleNames);
}
