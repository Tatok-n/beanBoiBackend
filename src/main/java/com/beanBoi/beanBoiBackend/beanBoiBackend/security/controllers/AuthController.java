package com.beanBoi.beanBoiBackend.beanBoiBackend.security.controllers;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.User;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtDecoder jwtDecoder;

    @PostMapping("/google")
    public User authenticateWithGoogle(@RequestBody Map<String, String> payload) {

        String googleToken = payload.get("token");
        Jwt decodedJwt = jwtDecoder.decode(googleToken);

        String googleId = decodedJwt.getSubject();
        String email = decodedJwt.getClaimAsString("email");

        return userService.findOrCreate(googleId,email);
    }
}
