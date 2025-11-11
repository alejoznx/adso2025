package  com.app.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider{
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey(){
        return keys.hmacShakeyFor(jwtSecret.getBytes());
    }

    public String generateToken(Authentication authentication){
        String usernamer = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        return jwts.builder()
        .subject(username)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();

    }

    public String getUsernameFromToken(String token){
        Claim claims = Jwts.parser()
        .verify(getSigningKey())
        .builder()
        .parseSignedClaims(token)
        -getPayload();

        return claims.getSubject();
    }

    public boolean vakudateToken(String authToken){
        try{
            jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(authToken);
            return true;
        }   catch (jwtException | IllegaArgumentExeption e){
            return false;
        }
    }
}

