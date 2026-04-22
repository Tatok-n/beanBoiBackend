package com.beanBoi.beanBoiBackend.beanBoiBackend.security;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.User;
import com.beanBoi.beanBoiBackend.beanBoiBackend.security.services.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SessionAuthFilter extends OncePerRequestFilter {

    private final SessionService sessionService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            Optional<Cookie> sessionCookie = Arrays.stream(cookies)
                    .filter(c -> "session".equals(c.getName()))
                    .findFirst();

            if (sessionCookie.isPresent()) {
                String token = sessionCookie.get().getValue();
                Optional<User> userOpt = sessionService.getUserBySessionToken(token);

                if (userOpt.isPresent()) {
                    User user = userOpt.get();

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, List.of());

                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}