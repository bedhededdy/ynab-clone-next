package com.ynab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ynab.repository.UserRepository;
import com.ynab.model.User;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    @Lazy // AuthenticationManager requires the UserService, so we need to specify @Lazy to resolve the circular dependency
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return getUserByEmail(email);
    }

    public String login(String email, String password) {
        // *ECP FIXME: NEED TO VERIFY THE USERNAME AND PASSWORD FIRST
        var usernamePassword = new UsernamePasswordAuthenticationToken(email, password);
        var authUser = authenticationManager.authenticate(usernamePassword);
        String accessToken = tokenService.generateAccessToken((User) authUser.getPrincipal());
        return accessToken;

    }

    public UserDetails register(String email, String password) throws Exception {
        if (getUserByEmail(email) != null)
            throw new Exception("User already exists");
        String encryptedPassword = new BCryptPasswordEncoder().encode(password);
        return userRepository.save(new User(email, encryptedPassword));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
