package org.nmng.library.manager.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.nmng.library.manager.dao.UserRepository;
import org.nmng.library.manager.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * This class intercept any request with a valid jwt and authenticate user by
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final RequestMatcher REQUEST_MATCHING_URL = new AntPathRequestMatcher("/**");
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

//    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationConverter authenticationConverter) {
//        super(authenticationManager, authenticationConverter);
//    }

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
//        super(REQUEST_MATCHING_URL, authenticationManager);

        // replacement of SimpleUrlAuthenticationFailureHandler
//        this.setAuthenticationFailureHandler((request, response, exception) -> {
//
//        });
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // IllegalStateException: cannot send response from authentication success/failure handler
        String header = request.getHeader("Authorization");

        // do not intercept requests containing the authorization header of invalid format
        if (!this.authorizationHeaderHasValidFormat(header)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = this.getAccessToken(header);
        String username = jwtUtils.getSubject(token);

        // null value occurs when the token is invalid, expired...
        // do not intercept requests containing invalid tokens
        if (username == null) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.unauthenticated(username, null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        try {
            // should call AuthenticationManager to get UserDetails
            authentication = (UsernamePasswordAuthenticationToken) this.authenticationManager.authenticate(authentication);
        } catch (LockedException ignored) {}

        this.afterAuthentication(request, response, filterChain, authentication);
    }

    protected void afterAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();

//        Check if user is being locked. If being, then authorities will not be set -> user is only considered he logged in.
        if (!userDetails.isAccountNonLocked()) {
            if (userDetails instanceof User user // should always be true
                    && LocalDateTime.now().isAfter((user).getLockExpirationDate())) {
                user.setLocked(false);
                this.userRepository.save(user);

                log.info("Unlocked user. Continue using service.");
            } else {
                authorities = null;

                log.info("User is still being locked. Continue using service as a restricted authenticated user.");
            }
        }

        UsernamePasswordAuthenticationToken savedAuth = UsernamePasswordAuthenticationToken.authenticated(
                authResult.getPrincipal(), null, authorities
        );
        savedAuth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(savedAuth);

        SecurityContextHolder.setContext(context);
    }

    private boolean authorizationHeaderHasValidFormat(String header) {
        return header != null && header.matches("Bearer [0-9a-zA-Z_-]+\\.[0-9a-zA-Z_-]+\\.[0-9a-zA-Z_-]+");
    }

    private String getAccessToken(String header) {
        return header.split("Bearer ")[1].trim();
    }

    private UserDetails getUserDetails(Claims token) {
        String username = token.getSubject();

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        if (userDetails == null) {
//            log.info("user not found");
            throw new UsernameNotFoundException("user not found");
        }

        return userDetails;
    }
}
