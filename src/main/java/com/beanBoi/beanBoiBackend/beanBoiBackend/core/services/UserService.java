package com.beanBoi.beanBoiBackend.beanBoiBackend.core.services;


import com.beanBoi.beanBoiBackend.beanBoiBackend.core.models.User;
import com.beanBoi.beanBoiBackend.beanBoiBackend.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User findOrCreate(String id, String email) {
        User user = userRepository.findById(id).orElse(new User());
        user.setEmail(email);
        user.setId(id);
        userRepository.save(user);
        return user;
    }

    public User getFromGoogleId(Jwt token) throws FileNotFoundException {
        String id = token.getSubject();
        return userRepository.findById(id).orElseThrow(FileNotFoundException::new);
    }
}
