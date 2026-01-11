package com.expanse_tracker.config.filter;

import com.expanse_tracker.config.jwt.JwtUtils;
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



        logger.info("JWT Authorization Filter ENTRO");
    String token = request.getHeader("Authorization");

    if(token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
        logger.info("JWT Authorization Filter TOKEN: " + token);
        if(jwtUtils.validateToken(token)) {
            String username = jwtUtils.extractUsername(token);
            String rolesStr = jwtUtils.extractRoles(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            logger.info("JWT Authorization Filter " + username + " - " + rolesStr);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
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
