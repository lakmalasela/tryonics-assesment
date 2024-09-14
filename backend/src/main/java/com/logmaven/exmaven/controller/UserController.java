package com.logmaven.exmaven.controller;


import com.logmaven.exmaven.entity.Role;
import com.logmaven.exmaven.entity.User;
import com.logmaven.exmaven.repository.RoleRepository;
import com.logmaven.exmaven.repository.UserRepository;
import com.logmaven.exmaven.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.*;

@RestController
public class UserController {


    @Autowired
    private AuthenticationManager authenticationManager ;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/createuser")
    public ResponseEntity<String> createAdminUser(@RequestBody User user) {
        // Validate input
        if (user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username and password are required");
        }

        User existingAdminUser = userRepository.findUserByUsername(user.getUsername());
        if (existingAdminUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        User adminUser = new User();
        adminUser.setUsername(user.getUsername());
        adminUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        adminUser.setAddedate(LocalDate.now());
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.getReferenceById(1));
        adminUser.setRoles(roles);

        userRepository.save(adminUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("Admin user created successfully!");
    }


    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        userRepository.delete(user);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<String> updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Basic validation for user input
        if (updatedUser.getUsername() == null || updatedUser.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username cannot be empty");
        }

        // Update user details
        existingUser.setUsername(updatedUser.getUsername());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));
        }

        // Ensure roles are set correctly
        Set<Role> roles = new HashSet<>();
        if (updatedUser.getRoles() != null) {
            for (Role role : updatedUser.getRoles()) {
                roles.add(roleRepository.getReferenceById(role.getId()));
            }
        }
        existingUser.setRoles(roles);

        userRepository.save(existingUser);
        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
    }



    @GetMapping("/findall")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





    //pagination
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        try {
            Pageable paging = PageRequest.of(page - 1, size);
            Page<User> pageUsers = userRepository.findAll(paging);

            Map<String, Object> response = new HashMap<>();
            response.put("users", pageUsers.getContent());
            response.put("currentPage", pageUsers.getNumber() + 1);
            response.put("totalItems", pageUsers.getTotalElements());
            response.put("totalPages", pageUsers.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(user);
    }


}
