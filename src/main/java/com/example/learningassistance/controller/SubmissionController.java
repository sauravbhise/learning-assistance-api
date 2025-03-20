package com.example.learningassistance.controller;

import com.example.learningassistance.model.Submission;
import com.example.learningassistance.repo.SubmissionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class SubmissionController {

    @Autowired
    SubmissionRepo submissionRepo;

    @GetMapping("/submissions")
    public ResponseEntity<List<Submission>> getAllSubmissions() {
        try {
        List<Submission> submissionsList = new ArrayList<>();
        submissionRepo.findAll().forEach(submissionsList::add);
            if (submissionsList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(submissionsList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/submissions/{id}")
    public ResponseEntity<Submission> getSubmissionById(@PathVariable long id) {
        Optional<Submission> submission = submissionRepo.findById(id);

        if (submission.isPresent()) {
            return new ResponseEntity<>(submission.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users/{userId}/submissions")
    public ResponseEntity<List<Submission>> getSubmissionsByStudentId(@PathVariable long userId) {
        try {
            List<Submission> submissionList = new ArrayList<>();
            submissionRepo.findByStudentId(userId).forEach(submissionList::add);

            if (submissionList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(submissionList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/assignments/{assignmentId}/submissions")
    public ResponseEntity<List<Submission>> getSubmissionsByAssignmentId(@PathVariable long assignmentId) {
        try {
            List<Submission> submissionList = new ArrayList<>();
            submissionRepo.findByAssignmentId(assignmentId).forEach(submissionList::add);

            if (submissionList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(submissionList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/submissions")
    public ResponseEntity<Submission> addSubmission(@RequestBody Submission submission) {
        Submission savedSubmission = submissionRepo.save(submission);

        return new ResponseEntity<>(savedSubmission, HttpStatus.OK);
    }

    @PutMapping("/submissions/{id}")
    public ResponseEntity<Submission> updateSubmissionById(@PathVariable long id, @RequestBody Submission newSubmissionData) {
        Optional<Submission> existingSubmission = submissionRepo.findById(id);

        if (existingSubmission.isPresent()) {
            Submission submission = existingSubmission.get();

            if (newSubmissionData.getFile_url() != null) {
                submission.setFile_url(newSubmissionData.getFile_url());
            }

            if (newSubmissionData.isEvaluted()) {
                submission.setEvaluted(true);
            } else {
                submission.setEvaluted(false);
            }

            Submission updatedSubmission = submissionRepo.save(submission);
            return new ResponseEntity<>(updatedSubmission, HttpStatus.OK);
        }

        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/submission/{id}")
    public ResponseEntity<HttpStatus> deleteSubmission(@PathVariable long id) {
        submissionRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
