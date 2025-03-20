package com.example.learningassistance.repo;

import com.example.learningassistance.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepo extends JpaRepository<Assignment, Long> {
    List<Assignment> findByCreatedBy(long createdBy);
}
