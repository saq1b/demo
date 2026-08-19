package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        
        // 1. Look for the "Authorization" header in the HTTP request
        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 2. Check if the header exists and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7); // Strip out "Bearer " to get the raw token
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Token is expired or invalid, leave username as null
            }
        }

        // 3. If a valid username is found and they aren't already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            if (jwtUtil.validateToken(jwt)) {
                // 4. Create an authentication object (empty list is for roles/permissions, which we're skipping for now)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>());
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 5. Tell Spring Security: "This user is officially logged in for this request"
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 6. Let the request continue to its destination (like your controller)
        chain.doFilter(request, response);
    }
}