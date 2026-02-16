package org.harshdev.ecom.controller;


import org.harshdev.ecom.Address;
import org.harshdev.ecom.User;
import org.harshdev.ecom.service.UserService;
import org.harshdev.ecom.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        String result = userService.registerUser(user);

        if (result.contains("already exists")) {
            return ResponseEntity.status(400).body(result);
        }

        return ResponseEntity.status(201).body(result);
    }

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id){
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id){
        userService.deleteUserById(id);
    }

    @DeleteMapping("/deleteAll")
    public void deleteAllUsers(){
        userService.deleteAllUsers();
    }

    @PutMapping("/{id}")
    public String updateById(@PathVariable String id, @RequestBody User user){
        return userService.updateUser(id, user);
    }

    @PostMapping("/login")
    public String login(@RequestBody org.harshdev.ecom.model.LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(//springsecurity checks if the user is correct or not
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            return jwtUtils.generateToken(loginRequest.getUsername());//is spring security authorize above,then gernrate token

        } catch (Exception e) {
            return "Bhai, galat details di tune. Access Denied!";
        }
    }

    @PutMapping("/profile/update")
    public ResponseEntity<User> updateProfile(Authentication auth, @RequestParam String fullName){
        return ResponseEntity.ok(userService.updateProfile(auth.getName(), fullName));
    }

    @PostMapping("/profile/address")
    public ResponseEntity<User> addAddress(Authentication auth, @RequestBody Address address) { // Gemini: RequestBody use karo
        return ResponseEntity.ok(userService.addAddress(auth.getName(), address));
    }

    @GetMapping("/profile/me")
    public ResponseEntity<User> getMyProfile(Authentication auth){
        return ResponseEntity.ok(userService.findByUsername(auth.getName()).orElseThrow(() -> new RuntimeException("User session expire ho gaya!")));
    }

}
