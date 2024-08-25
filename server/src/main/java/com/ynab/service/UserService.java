package com.ynab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ynab.repository.UserRepository;
import com.ynab.model.Budget;
import com.ynab.model.User;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Long login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            if (user.getPassword().equals(password))
                return user.getId();
        }
        return -1L;
    }

    public Long register(String email, String password) {
        if (userRepository.findByEmail(email) != null)
            return -1L;
        User user = new User(email, password);
        user = userRepository.save(user);
        return user.getId();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
