package org.harshdev.ecom.controller;


import org.harshdev.ecom.Product;
import org.harshdev.ecom.User;
import org.harshdev.ecom.service.ProductService;
import org.harshdev.ecom.service.UserService;
import org.harshdev.ecom.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        String result = service.registerUser(user);

        if (result.contains("already exists")) {
            return ResponseEntity.status(400).body(result); // Ab Postman mein 400 dikhayega
        }

        return ResponseEntity.status(201).body(result); // 201 matlab Created
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id){
        return service.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id){
        service.deleteUserById(id);
    }

    @DeleteMapping("/deleteAll")
    public void deleteAllUsers(){
        service.deleteAllUsers();
    }

    @PutMapping("/{id}")
    public String updateById(@PathVariable String id, @RequestBody User user){
        return service.updateUser(id, user);
    }

    @PostMapping("/login")
    public String login(@RequestBody org.harshdev.ecom.model.LoginRequest loginRequest) {
        try {
            // 1. Spring Security ko bolo ki check kare banda sahi h ya nahi
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // 2. Agar upar wali line fail nahi hui, matlab banda sahi h! Token generate karo.
            return jwtUtils.generateToken(loginRequest.getUsername());

        } catch (Exception e) {
            return "Bhai, galat details di tune. Access Denied!";
        }
    }
}
