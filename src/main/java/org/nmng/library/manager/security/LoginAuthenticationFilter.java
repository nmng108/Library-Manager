package org.nmng.library.manager.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.nmng.library.manager.dao.UserRepository;
import org.nmng.library.manager.dto.request.LoginDto;
import org.nmng.library.manager.dto.response.LoginResponse;
import org.nmng.library.manager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This class intercept any request with a valid jwt and authenticate user by
 */
@Component
@Slf4j
public class LoginAuthenticationFilter extends OncePerRequestFilter {
    public static final RequestMatcher REQUEST_MATCHING_URL = new AntPathRequestMatcher("/api/auth/login", HttpMethod.POST.name());
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!this.matchesRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // may catch exceptions here
        LoginDto dto = this.readBody(request);
        String username = dto.getUsername();
        String password = dto.getPassword();

        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        try {
            // should call AuthenticationManager to get UserDetails
            authentication = (UsernamePasswordAuthenticationToken) this.authenticationManager.authenticate(authentication);
        } catch (LockedException ignored) {
        }

        this.afterAuthentication(request, response, authentication);
    }

    protected void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        User user = (User) authResult.getPrincipal();
//        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

//        Check if user is being locked. If being, then authorities will not be set -> user is only considered he logged in.
        if (!user.isAccountNonLocked()) {
            if (LocalDateTime.now().isAfter((user).getLockExpirationDate())) {
                user.setLocked(false);
                this.userRepository.save(user);

                log.info("Unlocked user. Continue logging in.");
            } else {

                log.info("User is still being locked");
            }
        }

        // may set context between requests here?
//        UsernamePasswordAuthenticationToken savedAuth = UsernamePasswordAuthenticationToken.authenticated(
//                authResult.getPrincipal(), null, authorities
//        );
//        savedAuth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        context.setAuthentication(savedAuth);
//
//        SecurityContextHolder.setContext(context);

        this.sendResponse(response, user);
    }

    private boolean matchesRequest(HttpServletRequest request) {
        return LoginAuthenticationFilter.REQUEST_MATCHING_URL.matches(request);
    }

    private LoginDto readBody(HttpServletRequest request) {
        try {
            String stringifyBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            return new ObjectMapper().readValue(stringifyBody, LoginDto.class);
        } catch (IOException e) {
            log.error("Error while parsing login request body");
            throw new BadCredentialsException("Body is not of type JSON");
        }
    }

    private void sendResponse(HttpServletResponse response, User user) {
        String accessToken = this.jwtUtils.generateAccessToken(user);

        response.setHeader("Content-Type", "application/json;charset=utf-8");

        try {
            String responseBody = new ObjectMapper().writeValueAsString(new LoginResponse(accessToken));

            response.getWriter().write(responseBody);
            response.getWriter().flush();

        } catch (IOException e) {
            log.error("Error while creating response body");
            throw new AuthenticationServiceException("");
        }

    }
}
