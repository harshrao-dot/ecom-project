package org.harshdev.ecom.service;

import org.harshdev.ecom.Address;
import org.harshdev.ecom.User;
import org.harshdev.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Bhai, ye username pehle se booked h. Kuch naya try kar!";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); //encode password

        if(user.getRole() == null){
            user.setRole("ROLE_USER");
        }
        userRepository.save(user);
        return "User registered successfully with encoded password! and with role: " + user.getRole();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(String id){
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUserById(String id){
        userRepository.deleteById(id);
    }

    public void deleteAllUsers(){
        userRepository.deleteAll();
    }

    public String updateUser(String id, User newUser) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User old = optionalUser.get();
            old.setUsername(newUser.getUsername());
            old.setPassword(newUser.getPassword());
            userRepository.save(old);
            return "User update ho gaya!";
        }
        return "User nahi mila, check ID: " + id;
    }

    public User addAddress(String username, Address address){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User nahi mila!"));

        if(user.getAddresses() == null){
            user.setAddresses(new ArrayList<>());
        }

        user.getAddresses().add(address);

        return userRepository.save(user);
    }

    public User updateProfile(String username, String fullName){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("user nahi mila!"));

        user.setFullName(fullName);
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
