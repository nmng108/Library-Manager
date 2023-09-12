package org.nmng.library.manager.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.nmng.library.manager.dao.UserRepository;
import org.nmng.library.manager.entity.User;
import org.nmng.library.manager.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class JwtVerificationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (!hasValidAuthorizationBearer(header)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getAccessToken(header);

        if (!jwtUtils.validateAccessToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = getUserDetails(token);

        // check if user is locked. If not, then Authentication will be saved to the security context
        if (userDetails.isAccountNonLocked()) setAuthenticationContext(userDetails, request);
        else if (userDetails instanceof User && LocalDateTime.now().isAfter(((User) userDetails).getLockExpirationDate())) { // should always be true
            ((User) userDetails).setLocked(false);
            this.userRepository.save((User) userDetails);
            log.info("Unlocked user. Continue using service.");

            setAuthenticationContext(userDetails, request);
        } else {
            log.info("User is still being locked. Continue using service as an anonymous.");
        }

        filterChain.doFilter(request, response);
    }

    private boolean hasValidAuthorizationBearer(String header) {
        return header != null && header.matches("Bearer [.0-9a-zA-Z_-]+");
    }

    private String getAccessToken(String header) {
        return header.split("Bearer ")[1].trim();
    }

    private void setAuthenticationContext(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("authorities: {}", authentication.getAuthorities());
    }

    private UserDetails getUserDetails(String token) {
        String username = jwtUtils.getSubject(token);

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        if (userDetails == null) {
//            log.info("user not found");
            throw new AccessDeniedException("user not found");
        }

        return userDetails;
    }

//    private void changeLockStatus()
}
