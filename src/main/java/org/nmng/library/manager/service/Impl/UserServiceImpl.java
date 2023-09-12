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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(
                this.userRepository.findAll().stream().map(UserDto::new).collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<?>getAllUsers(String role) {
        Role specifiedRole = this.queryRole(role); // should have only 1 element

        if (specifiedRole == null) throw new RuntimeException("error while identifying role");

        return ResponseEntity.ok(
                this.userRepository.findByRole(specifiedRole).stream().map(UserDto::new).toList()
        );
    }

    /**
     * @param identifiable can be any of id, identityNumber and username.
     * @return
     */
    @Override
    public ResponseEntity<?> getSpecifiedUser(String identifiable) {
        User user = this.findUser(identifiable);

        return user == null ?
                ResponseEntity.notFound().build()
                :
                ResponseEntity.ok(new UserDto(user));
    }

    @Override
    public ResponseEntity<?>getSpecifiedUser(String identifiable, Role role) {
        User user = this.findUser(identifiable, role);

        return user == null ?
                ResponseEntity.notFound().build()
                :
                ResponseEntity.ok(new UserDto(user));
    }

    @Override
    public ResponseEntity<?> createUser(CreateUserDto dto) {
        User user = this.userRepository.save(dto.toUser());
        List<Role> roles = this.queryRoles(dto.getRoles());

        // concurrently save each userRole object and return list of them
        List<UserRole> userRoles = roles.stream()
                .map(role -> this.userRoleRepository.save(new UserRole(user, role)))
                .toList();

        user.setRoles(userRoles);

        return ResponseEntity.ok(new UserDto(user));
    }

    @Override
    public ResponseEntity<?>createUser(CreateUserDto dto, Role role) {
        User user = this.userRepository.save(dto.toUser());
        UserRole userRole = this.userRoleRepository.save(new UserRole(user, role));

        user.setRoles(List.of(userRole));

        return ResponseEntity.ok(new UserDto(user));
    }

    @Override
    public ResponseEntity<?> deleteUser(String identifiable) {
        User user = this.findUser(identifiable);

        if (user != null) this.userRepository.delete(user);
        else return ResponseEntity.notFound().build();

        return ResponseEntity.ok(new UserDto(user));
    }

    @Override
    public ResponseEntity<?> deleteUser(String identifiable, Role role) {
        User user = this.findUser(identifiable, role);

        if (user != null) this.userRepository.delete(user);
        else return ResponseEntity.noContent().build();

        return ResponseEntity.ok(new UserDto(user));
    }

    @Override
    public User findUser(String identifiable) {
        Optional<User> user = Optional.empty();

        try {
            long id = Long.parseUnsignedLong(identifiable);
            user = this.userRepository.findById(id);
        } catch (NumberFormatException ignored) {
        }

        // should throw not found exception
        return user.orElse(
                this.userRepository.findByUsername(identifiable).orElse(
                        this.userRepository.findByIdentityNumber(identifiable).orElse(null)
                )
        );
    }

    @Override
    public User findUser(String identifiable, Role role) {
        Optional<User> user = Optional.empty();

        try {
            long id = Long.parseUnsignedLong(identifiable);
            user = this.userRepository.findByIdAndRole(id, role);
        } catch (NumberFormatException ignored) {
        }

        // should throw not found exception
        return user.orElse(
                this.userRepository.findByUsernameAndRole(identifiable, role).orElse(
                        this.userRepository.findByIdentityNumberAndRole(identifiable, role).orElse(null)
                )
        );
    }

    /**
     * Validate input roles and convert all of them into the Role entities
     *
     * @param roleNames List<String>
     * @return List<Role>
     */
    @Override
    public List<Role> queryRoles(List<String> roleNames) {
        List<Role> allRoles = this.roleRepository.findAll();

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(404, "User not found", null);
        }

        List<Role> roles = this.userRoleRepository.findByUser(user).stream().map(UserRole::getRole).toList();
        List<? extends GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();

        user.setAuthorities(authorities);

        return user;
    }
}
