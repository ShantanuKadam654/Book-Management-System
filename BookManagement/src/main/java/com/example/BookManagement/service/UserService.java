package com.example.BookManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.BookManagement.model.user;
import com.example.BookManagement.repository.AdminRepository;
import com.example.BookManagement.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean authenticate(String username, String password) {
        user user = userRepository.findByUsername(username);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    public String registerUser(String username, String rawPassword, String email) 
    {
        if (userRepository.findByUsername(username) != null)
        {
            return "User already exists";
        }

        user user = new user();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEmail(email);
        userRepository.save(user);
        
        return "User registered successfully";
    }
    
    public String getUserEmailByUsername(String username) {
        user user = userRepository.findByUsername(username);
        return user != null ? user.getEmail() : null; 
    }
}
