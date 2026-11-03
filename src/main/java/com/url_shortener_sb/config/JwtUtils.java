package com.url_shortener_sb.config;

import com.url_shortener_sb.exception.UrlShortenerException;
import com.url_shortener_sb.service.serviceImpl.UserDetailsImpl;
import com.url_shortener_sb.service.serviceImpl.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.SignatureException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class JwtUtils {
    @Value("${app.jwt-secret}")
    private String secretKey;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;
//    @Value("${app.jwt-refresh-expiration-milliseconds}")
//    private int jwtRefreshExpirationInMs;

    public String generateToken(Authentication authentication) {
        Map<String, Object> claims
                = new HashMap<>();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put("roles", roles);
        return Jwts
                .builder()
                .claims(claims)
                .subject(authentication.getName())
                .issuer("MyAuthServer")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ jwtExpirationInMs))
                .signWith(generateKey())
                .compact();
    }

//    public String refreshToken(Authentication authentication) {
//        Map<String, Object> claims
//                = new HashMap<>();
//        return Jwts
//                .builder()
//                .claims()
//                .add(claims)
//                .subject(authentication.getName())
//                .issuer("MyAuthServer")
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis()+ jwtRefreshExpirationInMs))
//                .and()
//                .signWith(generateKey())
//                .compact();
//    }

    private SecretKey generateKey() {
        byte[] decode
                = Decoders.BASE64.decode(getSecretKey());

        return Keys.hmacShaKeyFor(decode);
    }


    public String getSecretKey() {
        return secretKey;
    }

    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimResolver) {
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public boolean validateToken(String token) throws SignatureException {
        try {
            Jwts.parser().verifyWith((SecretKey)generateKey()).build()
                .parseSignedClaims(token);
return true && !isTokenExpired(token);

        } catch (MalformedJwtException ex){
            throw new UrlShortenerException(HttpStatus.BAD_REQUEST,"Invalid Jwt Token");
        }catch (ExpiredJwtException ex){
            throw new UrlShortenerException(HttpStatus.BAD_REQUEST,"Expired Jwt Token");
        }catch (UnsupportedJwtException ex){
            throw new UrlShortenerException(HttpStatus.BAD_REQUEST,"Unsupported Jwt Token");
        }catch (IllegalArgumentException ex){
            throw new UrlShortenerException(HttpStatus.BAD_REQUEST,"Jwt claims string is empty");
        }
    }

}