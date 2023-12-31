package org.nmng.library.manager.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.nmng.library.manager.dao.RequestRepository;
import org.nmng.library.manager.dao.RoleRepository;
import org.nmng.library.manager.dao.UserRepository;
import org.nmng.library.manager.dao.UserRoleRepository;
import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.dto.request.LockUserDto;
import org.nmng.library.manager.dto.response.UserDto;
import org.nmng.library.manager.dto.response.common.SuccessResponse;
import org.nmng.library.manager.entity.*;
import org.nmng.library.manager.exception.InvalidRequestException;
import org.nmng.library.manager.exception.ResourceNotFoundException;
import org.nmng.library.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RequestRepository requestRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository,
                           RoleRepository roleRepository, RequestRepository requestRepository
    ) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.requestRepository = requestRepository;
    }

    @Lazy
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(
                this.userRepository.findAll().stream().map(UserDto::new).collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<?> getAllUsers(String role) {
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
    public ResponseEntity<?> getSpecifiedUser(String identifiable, Role serviceRole) {
        User user = this.findUser(identifiable, serviceRole);

        return user == null ?
                ResponseEntity.notFound().build()
                :
                ResponseEntity.ok(new UserDto(user));
    }

    @Override
    public ResponseEntity<?> createUser(CreateUserDto dto) {
        User user = dto.toUser();

        user.setPassword(this.passwordEncoder.encode(dto.getPassword()));

        User savedUser = this.userRepository.save(user);
        List<Role> roles = this.queryRoles(dto.getRoles());

        // concurrently save each userRole object and return list of them
        List<UserRole> userRoles = roles.stream()
                .map(role -> this.userRoleRepository.save(new UserRole(savedUser, role)))
                .toList();

        savedUser.setRoles(userRoles);

        return ResponseEntity.ok(new UserDto(savedUser));
    }

    @Override
    public ResponseEntity<?> createUser(User user, Role serviceRole) {
        User existedUser = this.userRepository.findByUsername(user.getUsername()).orElse(
                this.userRepository.findByIdentityNumber(user.getIdentityNumber()).orElse(null)
        );

        if (existedUser != null) throw new InvalidRequestException("User has existed");

        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        log.info("new password: {}", this.passwordEncoder.encode(user.getPassword()));
        User savedUser = this.userRepository.save(user);
        UserRole userRole = this.userRoleRepository.save(new UserRole(savedUser, serviceRole));

        savedUser.setRoles(List.of(userRole));

        return ResponseEntity.ok(new UserDto(savedUser));
    }

    @Override
    public ResponseEntity<?> deleteUser(String identifiable) {
        User user = this.findUser(identifiable);

        if (user != null) {
            List<Request> requests = this.requestRepository.findByPatron(user);

            for (Request request : requests) {
                if (request.getStatus().getName().equalsIgnoreCase(RequestStatus.BORROWING)
                        || request.getStatus().getName().equalsIgnoreCase(RequestStatus.EXPIRED)) {
                    throw new InvalidRequestException("User cannot be deleted. " +
                            "There's request with id \"%s\" in borrowing status");
                }

                this.requestRepository.delete(request);
            }

            this.userRepository.delete(user);
        } else return ResponseEntity.notFound().build();

        return ResponseEntity.ok(new UserDto(user));
    }

    @Override
    public ResponseEntity<?> deleteUser(String identifiable, Role serviceRole) {
        User user = this.findUser(identifiable, serviceRole);

        if (user != null) this.userRepository.delete(user);
        else return ResponseEntity.noContent().build();

        return ResponseEntity.ok(new UserDto(user));
    }

    @Override
    public ResponseEntity<?> lockUser(LockUserDto dto, Role serviceRole) {
        UserService.super.lockUser(dto, serviceRole);
        User user = this.findUser(dto.getIdentifiable(), serviceRole);

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        if (dto.isLocked()) {
            LocalDateTime expirationDate = LocalDateTime.now().plus(dto.getLastDuration());

            user.setLockExpirationDate(expirationDate);
            if (user.isAccountNonLocked()) user.setLocked(true);

            return ResponseEntity.ok(new SuccessResponse("user %s is locked until %s".formatted(user.getUsername(), expirationDate)));

        } else if (user.isLocked()) {
            user.setLocked(false);
            user.setLockExpirationDate(null);

            return ResponseEntity.ok(new SuccessResponse("user %s is unlocked".formatted(user.getUsername())));
        }

        return ResponseEntity.noContent().build();
    }

    // TODO: implement this; update all user's passwords with new passwordEncoder
    @Override
    public ResponseEntity<?> changeEncoder(String encoder) {
//        List<User> users = this.userRepository.findAll();
        return null;
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
    public User findUser(String identifiable, Role serviceRole) {
        Optional<User> user = Optional.empty();

        try {
            long id = Long.parseUnsignedLong(identifiable);
            user = this.userRepository.findByIdAndRole(id, serviceRole);
        } catch (NumberFormatException ignored) {
        }

        // should throw not found exception
        return user.orElse(
                this.userRepository.findByUsernameAndRole(identifiable, serviceRole).orElse(
                        this.userRepository.findByIdentityNumberAndRole(identifiable, serviceRole).orElse(null)
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
        log.info("loadUserByUsername is called");

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<Role> roles = this.roleRepository.findByUser(user);
        List<? extends GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();

        user.setAuthorities(authorities);

        return user;
    }
}
