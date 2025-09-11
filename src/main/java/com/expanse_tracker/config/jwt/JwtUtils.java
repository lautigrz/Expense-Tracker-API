package com.expanse_tracker.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration.time}")
    private String timeExpiration;

    public String generateToken(String username, Collection< ? extends GrantedAuthority> roles) {
        String rolesStr = getRoles(roles);
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", rolesStr)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try{

            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return true;

        }catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }


    public Key getSigningKey() {
       byte[] keyBytes = Decoders.BASE64.decode(secretKey);
         return Keys.hmacShaKeyFor(keyBytes);
    }

    private String getRoles(Collection<? extends GrantedAuthority> roles) {
        return roles.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }


}
