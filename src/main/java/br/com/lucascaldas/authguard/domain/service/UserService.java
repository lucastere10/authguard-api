package br.com.lucascaldas.authguard.domain.service;

import br.com.lucascaldas.authguard.domain.models.User;
import br.com.lucascaldas.authguard.domain.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        // Template for registering a user
        return null;
    }

    public Optional<User> login(String email, String password) {
        // Template for authenticating a user
        return Optional.empty();
    }
}
