package com.example.learningassistance.repo;

import com.example.learningassistance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByRole(String role);
}
