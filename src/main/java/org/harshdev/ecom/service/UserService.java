package org.harshdev.ecom.service;

import org.harshdev.ecom.Product;
import org.harshdev.ecom.User;
import org.harshdev.ecom.repository.ProductRepository;
import org.harshdev.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(User user) {
        // 1. Check if user already exists
        User existingUser = repository.findByUsername(user.getUsername());
        if (existingUser != null) {
            return "Bhai, ye username pehle se booked h. Kuch naya try kar!";
        }

        // 2. Encode password (Security step)
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if(user.getRole() == null){
            user.setRole("ROLE_USER");//default role is user
        }
        // 3. Save to DB
        repository.save(user);
        return "User registered successfully with encoded password! and with role: " + user.getRole();
    }

    public List<User> getAllUsers(){
        return repository.findAll();
    }

    public User getUserById(String id){
        return repository.findById(id).orElse(null);
    }

    public void deleteUserById(String id){
        repository.deleteById(id);
    }

    public void deleteAllUsers(){
        repository.deleteAll();
    }

    public String updateUser(String id, User newUser) {
        Optional<User> optionalUser = repository.findById(id);

        if (optionalUser.isPresent()) {
            User old = optionalUser.get();
            old.setUsername(newUser.getUsername());
            old.setPassword(newUser.getPassword());
            repository.save(old);
            return "User update ho gaya!";
        }
        return "User nahi mila, check ID: " + id;
    }
}
