package org.nmng.library.manager.controller;

import org.nmng.library.manager.dto.request.LoginDto;
import org.nmng.library.manager.dto.response.LoginResponse;
import org.nmng.library.manager.entity.User;
import org.nmng.library.manager.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/auth", "/api/auth/"})
public class AuthenticationController {
    AuthenticationManager authenticationManager;
    JwtUtils jwtUtils;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping({"/login", "/login/"})
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtUtils.generateAccessToken(user);
        LoginResponse response = new LoginResponse(user.getUsername(), accessToken);

        return ResponseEntity.ok(response);
    }

//    @PatchMapping({"/password", "/password/"})
//    public ResponseEntity<?> changePassword() {
//        return null;
//    }
}
