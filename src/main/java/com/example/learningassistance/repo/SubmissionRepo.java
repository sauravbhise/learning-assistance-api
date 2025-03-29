package com.example.learningassistance.repo;

import com.example.learningassistance.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepo extends JpaRepository<Submission, Long> {

    List<Submission> findByCreatedBy(long createdBy);

    List<Submission> findByAssignmentId(long assignmentId);
}
