package com.expanse_tracker.config.filter;

import com.expanse_tracker.config.jwt.JwtUtils;
import com.expanse_tracker.models.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private JwtUtils jwtUtils;
    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UserEntity user = null;

        String username = "";
        String password = "";

        try{
            user = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
            username = user.getUsername();
            password = user.getPassword();

        }catch (Exception e){
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);


        return getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();

        String token = jwtUtils.generateToken(user.getUsername(), user.getAuthorities());

        response.addHeader("Authorization",  token);

        Map<String, Object> body = new HashMap<>();
        body.put("token", token);

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
