package com.expanse_tracker.config.filter;

import com.expanse_tracker.enums.RateLimitType;
import com.expanse_tracker.service.RateLimitService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private final RateLimitService rateLimitService;

    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        RateLimitType type = resolveType(path);

        if (type == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String key = resolveKey(request, type);
        Bucket bucket = rateLimitService.resolveBucket(key, type);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(429);
            response.getWriter().write("Too Many Requests");
        }
    }

    private RateLimitType resolveType(String path){
        if (path.equals("/login")) return RateLimitType.LOGIN;
        if (path.equals("/auth/verify")) return RateLimitType.VERIFY;
        if (path.startsWith("/api")) return RateLimitType.AUTHENTICATED;
        return null;
    }


    private String resolveKey(HttpServletRequest request, RateLimitType type) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)){
            return auth.getName();
        }

        if(type == RateLimitType.LOGIN){
            String username = request.getParameter("username");
            return request.getRemoteAddr() + ":" + username;
        }

        return request.getRemoteAddr();
    }
}
