package com.expanse_tracker.config.filter;

import com.expanse_tracker.config.jwt.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;


    private static final Logger logger = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

    String header = request.getHeader("Authorization");

    if(header != null && header.startsWith("Bearer ")) {
        String token = header.substring(7);

        try {
            jwtUtils.validateToken(token);
            String username = jwtUtils.extractUsername(token);
            String rolesStr = jwtUtils.extractRoles(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            logger.info("JWT Authorization Filter " + username + " - " + rolesStr);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (ExpiredJwtException e) {
            logger.warn("Token expired");
            SecurityContextHolder.clearContext();
        } catch (JwtException e) {
            logger.warn("Invalid token");
            SecurityContextHolder.clearContext();
        }
    }

        filterChain.doFilter(request, response);

    }


    private Collection<? extends GrantedAuthority> extractAuthorities(String rolesStr) {
        String[] roles = rolesStr.split(",");

        Collection<? extends GrantedAuthority> authorities = List.of(roles).stream()
                .map(SimpleGrantedAuthority::new)
                .toList()
                ;

        log.info("autorizaciones" + authorities.toString());

        return authorities;

    }
}
