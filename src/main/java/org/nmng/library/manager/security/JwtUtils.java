package org.nmng.library.manager.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.nmng.library.manager.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtUtils {
    private static final long EXPIRE_DURATION = 60 * 2 * 1000; // unit: millisecond

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(String.format("%s", user.getUsername()))
                .setIssuer("libmng")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS256, this.SECRET_KEY)
                .compact();

    }

    public Claims parseJwt(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (IllegalArgumentException e) {
            log.info("Token is null, empty or only whitespace");
        } catch (MalformedJwtException e) {
            log.info("JWT is invalid", e);
        } catch (UnsupportedJwtException e) {
            log.info("JWT is not supported", e);
        } catch (SignatureException e) {
            log.info("Signature validation failed");
        } catch (ExpiredJwtException e) {
            log.info("JWT expired");
        }

        return null;
    }

    public boolean isAccessTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (IllegalArgumentException e) {
            log.info("Token is null, empty or only whitespace");
            throw e;
        } catch (MalformedJwtException e) {
            log.info("JWT is invalid", e);
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("JWT is not supported", e);
            throw e;
        } catch (SignatureException e) {
            log.info("Signature validation failed");
            throw e;
        } catch (ExpiredJwtException e) {
            log.info("JWT expired");
            throw e;
        }

//        return false;
    }

    public String getSubject(String token) {
        Claims claims = this.parseJwt(token);

        return claims != null ? claims.getSubject() : null;
    }

//    private Claims parseClaims(Jwt<?, Claims> token) {
//        return token.getBody();
//    }
}
