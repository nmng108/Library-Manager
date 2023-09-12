package org.nmng.library.manager.service.Impl;

import org.nmng.library.manager.dao.UserRepository;
import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.dto.request.LockUserDto;
import org.nmng.library.manager.dto.response.UserDto;
import org.nmng.library.manager.entity.Role;
import org.nmng.library.manager.entity.User;
import org.nmng.library.manager.exception.HttpException;
import org.nmng.library.manager.service.LibrarianService;
import org.nmng.library.manager.service.PatronService;
import org.nmng.library.manager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibrarianServiceImpl implements LibrarianService {
    private final UserService userService;
    private final Role ROLE;

    public LibrarianServiceImpl(UserService userService) {
        this.userService = userService;
        this.ROLE = userService.queryRole(ROLE_NAME);
    }

    @Override
    public ResponseEntity<?> getAllUsers() {
        return this.userService.getAllUsers(ROLE_NAME);
    }

    @Override
    public ResponseEntity<?> getSpecifiedUser(String identifiable) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null) throw new HttpException(403, "not permitted");

        // only allow patron to view info of the logged in account and prevent patron from viewing info of others
        boolean isCurrentUserLibrarian = user.getAuthorities().stream().anyMatch(grantedAuthority ->
                grantedAuthority.getAuthority().equalsIgnoreCase(Role.LIBRARIAN));

        boolean matchedIdentifiable = identifiable.equals(user.getUsername())
                || identifiable.equals(String.valueOf(user.getId()))
                || identifiable.equals(String.valueOf(user.getIdentityNumber()));

        if (isCurrentUserLibrarian && !matchedIdentifiable) {
            throw new HttpException(403, "not permitted");
        }

        return this.userService.getSpecifiedUser(identifiable, this.ROLE);
    }

    @Override
    public ResponseEntity<?> createUser(CreateUserDto dto) {
        return this.userService.createUser(dto, this.ROLE);
    }

    @Override
    public ResponseEntity<?> deleteUser(String identifiable) {
        return this.userService.deleteUser(identifiable, this.ROLE);
    }

    @Override
    public ResponseEntity<?> lockUser(LockUserDto dto) {
        LibrarianService.super.lockUser(dto);
        return this.userService.lockUser(dto, this.ROLE);
    }
}
