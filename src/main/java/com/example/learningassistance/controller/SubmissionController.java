package com.example.learningassistance.controller;

import com.example.learningassistance.model.Assignment;
import com.example.learningassistance.model.Submission;
import com.example.learningassistance.repo.AssignmentRepo;
import com.example.learningassistance.repo.SubmissionRepo;
import com.example.learningassistance.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class SubmissionController {

    @Autowired
    SubmissionRepo submissionRepo;
    @Autowired
    AssignmentRepo assignmentRepo;
    @Autowired
    FileService fileService;

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
            submissionRepo.findByCreatedBy(userId).forEach(submissionList::add);

            if (submissionList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(submissionList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/students/{studentId}/assignments/{assignmentId}/submission")
    public ResponseEntity<Submission> getSubmissionByAssignmentIdAndStudentId(@PathVariable long assignmentId, @PathVariable long studentId) {
        try {
            Optional<Submission> submission = submissionRepo.findByAssignmentIdAndCreatedBy(assignmentId, studentId);

            if (!submission.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(submission.get(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/la/{laId}/submissions")
    public ResponseEntity<List<Submission>> getSubmissionsByLatId(@PathVariable long laId) {
        try {
            List<Assignment> assignmentList = new ArrayList<>();

            assignmentRepo.findByCreatedBy(laId).forEach(assignmentList::add);

            List<Submission> submissionList = new ArrayList<>();

            assignmentList.forEach(assignment -> {
                submissionRepo.findByAssignmentId(assignment.getId()).forEach(submissionList::add);
            });

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
    public ResponseEntity<Submission> addSubmission(@ModelAttribute Submission submission, @RequestParam MultipartFile file) {

        try {
            String filePath = fileService.uploadFile(file);

            submission.setFilePath(filePath);

            Submission savedSubmission = submissionRepo.save(submission);

            return new ResponseEntity<>(savedSubmission, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/submissions/{id}")
    public ResponseEntity<Submission> updateSubmissionById(@PathVariable long id, @RequestBody Submission newSubmissionData) {
        Optional<Submission> existingSubmission = submissionRepo.findById(id);

        if (existingSubmission.isPresent()) {
            Submission submission = existingSubmission.get();

            if (newSubmissionData.getFilePath() != null) {
                submission.setFilePath(newSubmissionData.getFilePath());
            }

            if (newSubmissionData.isEvaluated()) {
                submission.setEvaluated(true);
            } else {
                submission.setEvaluated(false);
            }

            Submission updatedSubmission = submissionRepo.save(submission);
            return new ResponseEntity<>(updatedSubmission, HttpStatus.OK);
        }

        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/submissions/{id}")
    public ResponseEntity<HttpStatus> deleteSubmission(@PathVariable long id) {
        submissionRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
