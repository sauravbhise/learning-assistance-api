package com.example.learningassistance.controller;

import com.example.learningassistance.model.Evaluation;
import com.example.learningassistance.model.Submission;
import com.example.learningassistance.repo.EvaluationRepo;
import com.example.learningassistance.repo.SubmissionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class EvaluationController {

    @Autowired
    EvaluationRepo evaluationRepo;
    @Autowired
    SubmissionRepo submissionRepo;

    @GetMapping("/evaluations")
    public ResponseEntity<List<Evaluation>> getAllEvaluations() {
        try {
            List<Evaluation> evaluationList = new ArrayList<>();

            evaluationRepo.findAll().forEach(evaluationList::add);
            if (evaluationList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(evaluationList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/evaluations/{id}")
    public ResponseEntity<Evaluation> getEvaluationById(@PathVariable long id) {
        Optional<Evaluation> evaluation = evaluationRepo.findById(id);

        if (evaluation.isPresent()) {
            return new ResponseEntity<>(evaluation.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users/{userId}/evaluations")
    public ResponseEntity<List<Evaluation>> getEvaluationsByEvaluatorId(@PathVariable long userId) {
        try {
            List<Evaluation> evaluationList = new ArrayList<>();
            evaluationRepo.findByEvaluatorId(userId).forEach(evaluationList::add);

            if (evaluationList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(evaluationList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/submissions/{submissionId}/evaluation")
    public ResponseEntity<Evaluation> getEvaluationBySubmissionId(@PathVariable long submissionId) {
        try {
            Evaluation evaluation = evaluationRepo.findBySubmissionId(submissionId);

            return new ResponseEntity<>(evaluation, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/evaluations")
    public ResponseEntity<Evaluation> addEvaluation(@RequestBody Evaluation evaluation) {

        Optional<Submission> submissionOptional = submissionRepo.findById(evaluation.getSubmissionId());

        if (!submissionOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Evaluation savedEvaluation = evaluationRepo.save(evaluation);

        Submission submission = submissionOptional.get();
        submission.setEvaluated(true);
        submissionRepo.save(submission);

        return new ResponseEntity<>(savedEvaluation, HttpStatus.OK);
    }

    @PutMapping("/evaluations/{id}")
    public ResponseEntity<Evaluation> updateEvaluationById(@PathVariable long id, @RequestBody Evaluation newEvaluationData) {
        Optional<Evaluation> existingEvaluation = evaluationRepo.findById(id);

        if (existingEvaluation.isPresent()) {
            Evaluation evaluation = existingEvaluation.get();

            if (newEvaluationData.getFeedback() != null) {
                evaluation.setFeedback(newEvaluationData.getFeedback());
            }

            if (newEvaluationData.getScore() > -1) {
                evaluation.setScore(newEvaluationData.getScore());
            }

            Evaluation updatedEvaluation = evaluationRepo.save(evaluation);
            return new ResponseEntity<>(updatedEvaluation, HttpStatus.OK);
        }

        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/evaluations/{id}")
    public ResponseEntity<HttpStatus> deleteEvaluation(@PathVariable long id) {
        evaluationRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
