package com.beanBoi.beanBoiBackend.beanBoiBackend.security.controllers;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.User;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.services.UserService;
import com.beanBoi.beanBoiBackend.beanBoiBackend.security.services.SessionService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtDecoder jwtDecoder;

    @Autowired
    SessionService sessionService;

    @PostMapping("/google")
    public ResponseEntity<User>  authenticateWithGoogle(@RequestBody Map<String, String> payload, HttpServletResponse response) {

        String googleToken = payload.get("token");
        Jwt decodedJwt = jwtDecoder.decode(googleToken);

        String googleId = decodedJwt.getSubject();
        String email = decodedJwt.getClaimAsString("email");

        User user = userService.findOrCreate(googleId,email);

        String appSessionToken = sessionService.createSessionToken(user);

        ResponseCookie cookie = ResponseCookie.from("session", appSessionToken)
                   .httpOnly(true)
                   .secure(true)
                   .sameSite("Lax")
                   .path("/")
                   .maxAge(Duration.ofDays(7))
                   .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(user);
    }
}
