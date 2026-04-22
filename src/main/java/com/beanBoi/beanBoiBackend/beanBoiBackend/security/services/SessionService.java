package com.beanBoi.beanBoiBackend.beanBoiBackend.security.services;

import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {

    private final Map<String, User> activeSessions = new ConcurrentHashMap<>();

    public String createSessionToken(User user) {
        String token = UUID.randomUUID().toString();
        activeSessions.put(token, user);
        return token;
    }

    public Optional<User> getUserBySessionToken(String token) {
        return Optional.ofNullable(activeSessions.get(token));
    }

    public void removeSession(String token) {
        activeSessions.remove(token);
    }
}
