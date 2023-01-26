package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.AuthenticationResponse;
import com.example.demo.config.JwtUtil;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    public JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) throws Exception {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
        }
            catch(BadCredentialsException e)
            {
                throw new Exception("Username or Password are incorrect.");
            }
            final UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
    @GetMapping("/hey")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hey api user");
    }

    @PostMapping("/insertUser")
    public void insert(@RequestBody User user) {
        userService.insertUser(user);
    }

    @GetMapping("/users/profile")
    public Optional<User> profile(Principal principal) {
        String un = principal.getName();
        return userService.findByEmail(un);
    }

    @GetMapping("/allUsers")
    public List<User> allUsers() {
        return userService.findAll();
    }

    @GetMapping("/users")
    public ResponseEntity<String> showUsers() {
        return ResponseEntity.ok("Hey users only see this!");
    }

    @GetMapping("/users/{id}")
    public Optional<User> findById(@PathVariable int id) {
        return userService.findById(id);
    }

}
