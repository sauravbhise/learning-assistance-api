package com.example.learningassistance.controller;

import com.example.learningassistance.model.Assignment;
import com.example.learningassistance.repo.AssignmentRepo;
import com.example.learningassistance.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class AssignmentController {

    @Autowired
    private AssignmentRepo assignmentRepo;
    @Autowired
    private FileService fileService;

    @GetMapping("/assignments")
    public ResponseEntity<List<Assignment>> getAllAssignments() {
        try {
            List<Assignment> assignmentList = new ArrayList<>();
            assignmentRepo.findAll().forEach(assignmentList::add);

            if (assignmentList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(assignmentList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/assignments/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable long id) {
        Optional<Assignment> assignment = assignmentRepo.findById(id);

        if (assignment.isPresent()) {
            return new ResponseEntity<>(assignment.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users/{userId}/assignments")
    public ResponseEntity<List<Assignment>> getAssignmentsByLaId(@PathVariable long userId) {
        try {
            List<Assignment> assignmentList = new ArrayList<>();
            assignmentRepo.findByCreatedBy(userId).forEach(assignmentList::add);

            if (assignmentList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(assignmentList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/assignments")
    public ResponseEntity<Assignment> addAssignment(@ModelAttribute Assignment assignment, @RequestParam MultipartFile file) {

        try {
            String filePath = fileService.uploadFile(file);

            assignment.setFilePath(filePath);

            Assignment savedAssignment = assignmentRepo.save(assignment);

            return new ResponseEntity<>(savedAssignment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/assignments/{id}")
    public ResponseEntity<Assignment> updateAssignmentById(@PathVariable long id, @RequestBody Assignment newAssignmentData) {
        Optional<Assignment> existingAssignment = assignmentRepo.findById(id);

        if (existingAssignment.isPresent()) {
            Assignment assignment = existingAssignment.get();

            if (newAssignmentData.getTitle() != null) {
                assignment.setTitle(newAssignmentData.getTitle());
            }

            if (newAssignmentData.getDescription() != null) {
                assignment.setDescription(newAssignmentData.getDescription());
            }

            if (newAssignmentData.getFilePath() != null) {
                assignment.setFilePath(newAssignmentData.getFilePath());
            }

            Assignment updatedAssignment = assignmentRepo.save(assignment);
            return new ResponseEntity<>(updatedAssignment, HttpStatus.OK);
        }

        return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<HttpStatus> deleteAssignment(@PathVariable long id) {
        assignmentRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
