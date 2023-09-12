package org.nmng.library.manager.service.Impl;

import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.dto.request.LockUserDto;
import org.nmng.library.manager.entity.Role;
import org.nmng.library.manager.entity.User;
import org.nmng.library.manager.exception.HttpException;
import org.nmng.library.manager.service.PatronService;
import org.nmng.library.manager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PatronServiceImpl implements PatronService {
    private final UserService userService;
    private final Role ROLE;

    public PatronServiceImpl(UserService userService) {
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

        boolean isCurrentUserPatron = user.getAuthorities().stream().anyMatch(grantedAuthority ->
                grantedAuthority.getAuthority().equalsIgnoreCase(Role.PATRON));
        boolean matchedIdentifiable = identifiable.equals(user.getUsername())
                || identifiable.equals(String.valueOf(user.getId()))
                || identifiable.equals(String.valueOf(user.getIdentityNumber()));

        if (isCurrentUserPatron && !matchedIdentifiable) {
            System.out.println(user.getUsername() + " cannot access info of the user " + identifiable);
            throw new HttpException(403, "E03", "not permitted");
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
        PatronService.super.lockUser(dto);
        return this.userService.lockUser(dto, this.ROLE);
    }
}
