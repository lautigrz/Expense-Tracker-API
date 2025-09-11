package com.expanse_tracker.config.filter;

import com.expanse_tracker.config.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

    String token = request.getHeader("Authorization");

    if(token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
        if(jwtUtils.validateToken(token)) {
            String username = jwtUtils.extractUsername(token);
            String rolesStr = jwtUtils.extractRoles(token);

           // UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, extractAuthorities(rolesStr));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }


    }

        filterChain.doFilter(request, response);

    }


    private List<GrantedAuthority> extractAuthorities(String rolesStr) {
        String[] roles = rolesStr.split(",");
        return List.of(roles).stream()
                .map(role -> (GrantedAuthority) () -> role)
                .toList();
    }
}
