package org.nmng.library.manager.security;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.nmng.library.manager.dao.UserRepository;
import org.nmng.library.manager.entity.User;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * Basically like DaoAuthenticationProvider but do not authenticate by password. Instead, this class mainly
 * checks and handles lock state of the current user owning a jwt.
 */
@Getter
@Setter
@Slf4j
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private UserDetailsService userDetailsService;
    private UserRepository userRepository;

    public JwtAuthenticationProvider() {
    }

    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
//        User user = (User) userDetails;
//
//        // user with temporary unlocked state will contain a non-null expiration date
//        if (user.getLockExpirationDate() != null) {
//            // if user's lock date has expired
//            if (LocalDateTime.now().isAfter(user.getLockExpirationDate())) {
//                user.setLockExpirationDate(null);
//                this.userRepository.save(user);
//            } else {
//                // if user is still being locked
////                user.setLocked(true); // useless
//                user.setAuthorities(Collections.emptyList());
//            }
//        }
    }

    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        try {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
            }

            // check if user's lock has expired before the framework's checking method
            if (!userDetails.isAccountNonLocked()) {
                // temporarily set locked to false in order to pass the pre-check method
                if (userDetails instanceof User user) { // should always be true
                    if (LocalDateTime.now().isAfter(user.getLockExpirationDate())) {
                        user.setLocked(false);

                        log.info("Unlocked user. Continue using service.");
                    } else { // test
                        log.info("User is still being locked. Continue using service as an anonymous.");
                    }
                } else {
                    throw new InternalAuthenticationServiceException("userDetails is not of type User");
                }
            }

            return userDetails;
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }
    }

    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        return super.createSuccessAuthentication(principal, authentication, user);
    }
}
