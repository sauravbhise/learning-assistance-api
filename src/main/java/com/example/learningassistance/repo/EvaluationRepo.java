package com.example.learningassistance.repo;

import com.example.learningassistance.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepo extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByEvaluatorId(long evaluatorId);

    Evaluation findBySubmissionId(long submissionId);
}
