package com.logmaven.exmaven.controller;

import com.logmaven.exmaven.service.MyUserDetailService;
import com.logmaven.exmaven.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            String username = loginDetails.get("username");
            String password = loginDetails.get("password");

            // Authenticate the user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            // Load user details
            UserDetails userDetails = userDetailService.loadUserByUsername(username);

            // Generate JWT token
            String token = jwtUtil.generateToken(userDetails.getUsername());

            // Prepare response
            response.put("token", token);
            response.put("message", "Login successful");
            response.put("status", "success");
        } catch (AuthenticationException e) {
            response.put("message", "Invalid username or password");
            response.put("status", "error");
        } catch (Exception e) {
            response.put("message", "An error occurred");
            response.put("status", "error");
        }
        return response;
    }
}
