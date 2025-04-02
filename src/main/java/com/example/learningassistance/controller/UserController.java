package com.example.learningassistance.controller;

import com.example.learningassistance.model.User;
import com.example.learningassistance.repo.LaStudentMappingRepo;
import com.example.learningassistance.repo.UserRepo;
import com.example.learningassistance.service.JwtService;
import com.example.learningassistance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private LaStudentMappingRepo laStudentMappingRepo;
    @Autowired
    private UserService userService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;

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

    @GetMapping("/users/admins")
    public ResponseEntity<List<User>> getAllAdmins() {
        try {
            List<User> adminList = new ArrayList<>();
            userRepo.findByRole("ADMIN").forEach(adminList::add);

            if (adminList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(adminList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/las")
    public ResponseEntity<List<User>> getAllLas() {
        try {
            List<User> laList = new ArrayList<>();
            userRepo.findByRole("LA").forEach(laList::add);

            if (laList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(laList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/students")
    public ResponseEntity<List<User>> getAllStudents() {
        try {
            List<User> studentList = new ArrayList<>();
            userRepo.findByRole("STUDENT").forEach(studentList::add);

            if (studentList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(studentList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/students/unassigned")
    public ResponseEntity<List<User>> getAllUnassignedStudents() {
        try {
            List<User> studentList = new ArrayList<>();
            userRepo.findByRole("STUDENT").forEach(studentList::add);

            if (studentList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<User> unassignedStudentList = new ArrayList<>();

            studentList.forEach(student -> {
                if(laStudentMappingRepo.findByStudentId(student.getId()) == null) {
                    unassignedStudentList.add(student);
                }
            });

            return new ResponseEntity<>(unassignedStudentList, HttpStatus.OK);

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
        Optional<User> existingUser = Optional.ofNullable(userRepo.findByEmail(user.getEmail()));

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

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        Optional<User> existingUser = Optional.ofNullable(userRepo.findByEmail(user.getEmail()));

        if (existingUser.isPresent()) {
            return new ResponseEntity<>("User with email " + user.getEmail() + " already exists.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            String accessToken = jwtService.generateToken(user.getEmail());
            String role = userRepo.findByEmail(user.getEmail()).getRole();
            Long id = userRepo.findByEmail(user.getEmail()).getId();
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("role", role);
            response.put("id", String.valueOf(id));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
