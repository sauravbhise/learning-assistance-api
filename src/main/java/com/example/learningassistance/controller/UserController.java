package com.example.learningassistance.controller;

import com.example.learningassistance.model.User;
import com.example.learningassistance.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {

            List<User> userList = new ArrayList<>();
            userRepo.findAll().forEach(userList::add);

            if (userList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(userList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        Optional<User> user = userRepo.findById(id);

        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        Optional<User> existingUser = userRepo.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            return new ResponseEntity<>("User with email " + user.getEmail() + " already exists.", HttpStatus.CONFLICT);
        }

        User savedUser = userRepo.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable long id, @RequestBody User newUserData) {
        Optional<User> existingUser = userRepo.findById(id);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            if (newUserData.getEmail() != null) {
                user.setEmail(newUserData.getEmail());
            }
            if (newUserData.getPassword() != null) {
                user.setPassword(newUserData.getPassword());
            }
            if (newUserData.getRole() != null) {
                user.setRole(newUserData.getRole());
            }

            User updatedUser = userRepo.save(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable long id) {
        userRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
