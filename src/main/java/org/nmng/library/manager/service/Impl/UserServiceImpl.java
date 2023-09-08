package org.nmng.library.manager.service.Impl;

import org.nmng.library.manager.dao.RoleRepository;
import org.nmng.library.manager.dao.UserRepository;
import org.nmng.library.manager.dao.UserRoleRepository;
import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.dto.response.UserDto;
import org.nmng.library.manager.entity.Role;
import org.nmng.library.manager.entity.User;
import org.nmng.library.manager.entity.UserRole;
import org.nmng.library.manager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return this.userRepository.findAll().stream().map(UserDto::new).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getAllUsers(String role) {
        Role specifiedRole = this.queryRole(role); // should have only 1 element

        if (specifiedRole == null) throw new RuntimeException("error while identifying role");
        return this.userRepository.findByRole(specifiedRole).stream().map(UserDto::new).toList();
    }

    /**
     * @param identifier can be any of id, identityNumber and username.
     * @return
     */
    @Override
    public Object getSpecifiedUser(String identifier) {
        User user = this.findUser(identifier);

        return user == null ?
                ResponseEntity.notFound().build()
                :
                ResponseEntity.ok(new UserDto(user));
    }

    @Override
    public Object getSpecifiedUser(String identifier, Role role) {
        User user = this.findUser(identifier, role);

        return user == null ?
                ResponseEntity.notFound().build()
                :
                ResponseEntity.ok(new UserDto(user));
    }

    @Override
    public Object createUser(CreateUserDto dto) {
        User user = this.userRepository.save(dto.toUser());
        List<Role> roles = this.queryRoles(dto.getRoles());

        // concurrently save each userRole object and return list of them
        List<UserRole> userRoles = roles.stream()
                .map(role -> this.userRoleRepository.save(new UserRole(user, role)))
                .toList();

        user.setRoles(userRoles);

        return new UserDto(user);
    }

    @Override
    public Object createUser(CreateUserDto dto, Role role) {
        User user = this.userRepository.save(dto.toUser());
        UserRole userRole = this.userRoleRepository.save(new UserRole(user, role));

        user.setRoles(List.of(userRole));

        return new UserDto(user);
    }

    @Override
    public Object deleteUser(String identifier) {
        User user = this.findUser(identifier);

        if (user != null) this.userRepository.delete(user);
        else return ResponseEntity.notFound().build();

        return ResponseEntity.ok(new UserDto(user));
    }

    @Override
    public Object deleteUser(String identifier, Role role) {
        User user = this.findUser(identifier, role);

        if (user != null) this.userRepository.delete(user);
        else return ResponseEntity.notFound().build();

        return ResponseEntity.ok(new UserDto(user));
    }

    @Override
    public User findUser(String identifier) {
        Optional<User> user = Optional.empty();

        try {
            long id = Long.parseUnsignedLong(identifier);
            user = this.userRepository.findById(id);
        } catch (NumberFormatException ignored) {
        }

        // should throw not found exception
        return user.orElse(
                this.userRepository.findByUsername(identifier).orElse(
                        this.userRepository.findByIdentityNumber(identifier).orElse(null)
                )
        );
    }

    @Override
    public User findUser(String identifier, Role role) {
        Optional<User> user = Optional.empty();

        try {
            long id = Long.parseUnsignedLong(identifier);
            user = this.userRepository.findByIdAndRole(id, role);
        } catch (NumberFormatException ignored) {
        }

        // should throw not found exception
        return user.orElse(
                this.userRepository.findByUsernameAndRole(identifier, role).orElse(
                        this.userRepository.findByIdentityNumberAndRole(identifier, role).orElse(null)
                )
        );
    }

    /**
     * Validate input roles and convert all of them into the Role objects
     *
     * @param roleNames List<String>
     * @return List<Role>
     */
    @Override
    public List<Role> queryRoles(List<String> roleNames) {
        List<Role> allRoles = roleRepository.findAll();

        return roleNames.stream().map(roleName -> {
            Role result = null;

            for (Role role : allRoles) {
                if (role.getName().compareToIgnoreCase(roleName) == 0) {
                    result = role;
                    break;
                }
            }

            if (result == null) throw new RuntimeException("The role '%s' not found".formatted(roleName));

            return result;
        }).toList();
    }

    @Override
    public Role queryRole(String roleName) {
        return this.queryRoles(List.of(roleName)).get(0);
    }
}
