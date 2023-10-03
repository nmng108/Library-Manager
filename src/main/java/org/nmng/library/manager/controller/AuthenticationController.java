package org.nmng.library.manager.controller;

import org.nmng.library.manager.dao.UserRepository;
import org.nmng.library.manager.dto.request.CreatePatronDto;
import org.nmng.library.manager.dto.request.CreateUserDto;
import org.nmng.library.manager.dto.request.LoginDto;
import org.nmng.library.manager.dto.response.LoginResponse;
import org.nmng.library.manager.entity.User;
import org.nmng.library.manager.exception.HttpException;
import org.nmng.library.manager.security.JwtUtils;
import org.nmng.library.manager.service.PatronService;
import org.nmng.library.manager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping({"/api/auth", "/api/auth/"})
public class AuthenticationController {
    private final UserService userService;
    private final PatronService patronService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthenticationController(UserService userService, PatronService patronService, UserRepository userRepository,
                                    AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.patronService = patronService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

//    @PostMapping({"/login", "/login/"})
//    @Transactional
//    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
//        User user = (User) this.userService.loadUserByUsername(dto.getUsername());
//
//        if (user == null) throw new HttpException(401, "username or password is wrong");
//        // check if user's lock mode has expired. If true, then unlock and allow user to login
//        if (user.isLocked() && LocalDateTime.now().isAfter(user.getLockExpirationDate())) {
//            user.setLocked(false);
//            this.userRepository.save(user);
//        } else if (user.isLocked()) {}
//
//        Authentication authentication = this.authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(user, dto.getPassword())
//        );
//
//        String accessToken = jwtUtils.generateAccessToken(user);
//        LoginResponse response = new LoginResponse(accessToken);
//
//        return ResponseEntity.ok(response);
//    }

    @PostMapping({"/register", "/register/"})
    public ResponseEntity<?> registerPatron(@RequestBody CreatePatronDto dto) {
        return this.patronService.createUser(dto);
    }

    private void changeLockStatus() {

    }
//    @PatchMapping({"/password", "/password/"})
//    public ResponseEntity<?> changePassword() {
//        return null;
//    }
}
