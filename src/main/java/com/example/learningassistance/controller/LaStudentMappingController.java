package com.example.learningassistance.controller;

import com.example.learningassistance.model.LaStudentMapping;
import com.example.learningassistance.model.User;
import com.example.learningassistance.repo.LaStudentMappingRepo;
import com.example.learningassistance.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class LaStudentMappingController {

    @Autowired
    LaStudentMappingRepo laStudentMappingRepo;
    @Autowired
    UserRepo userRepo;

    @GetMapping("/la-students")
    public ResponseEntity<List<LaStudentMapping>> getAllMappings() {
        try {
            List<LaStudentMapping> mappingList = new ArrayList<>();

            laStudentMappingRepo.findAll().forEach(mappingList::add);
            if (mappingList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(mappingList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/la-students/{id}")
    public ResponseEntity<LaStudentMapping> getMappingById(@PathVariable long id) {
        Optional<LaStudentMapping> mapping = laStudentMappingRepo.findById(id);

        if (mapping.isPresent()) {
            return new ResponseEntity<>(mapping.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/la-students/{laId}/students/{studentId}")
    public ResponseEntity<Optional<LaStudentMapping>> getMappingBylaIdandStudent(@PathVariable long laId, @PathVariable long studentId) {
        try {
            List<LaStudentMapping> mappingList = new ArrayList<>();
            laStudentMappingRepo.findByLaId(laId).forEach(mappingList::add);

            if (mappingList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            Optional<LaStudentMapping> studentMapping = mappingList.stream()
                    .filter(mapping -> mapping.getStudentId() == studentId).findFirst();

            if (studentMapping.isPresent()) {
                return new ResponseEntity<>(studentMapping, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/la/{laId}/students")
    public ResponseEntity<List<Optional<User>>> getMappingByLaId(@PathVariable long laId) {
        try {
            List<LaStudentMapping> mappingList = new ArrayList<>();
            laStudentMappingRepo.findByLaId(laId).forEach(mappingList::add);

            if (mappingList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<Optional<User>> studentList = new ArrayList<>();

            mappingList.forEach(mapping -> {
                studentList.add(userRepo.findById(mapping.getStudentId()));
            });

            return new ResponseEntity<>(studentList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/students/{studentId}/la")
    public ResponseEntity<Optional<User>> getLaByStudentId(@PathVariable long studentId) {
        try {
            LaStudentMapping mapping = laStudentMappingRepo.findByStudentId(studentId);

            if (mapping == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Optional<User> la = userRepo.findById(mapping.getLaId());

            if (!la.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(la, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/la-students")
    public ResponseEntity<LaStudentMapping> addMapping(@RequestBody LaStudentMapping mapping) {
        LaStudentMapping savedMapping = laStudentMappingRepo.save(mapping);

        return new ResponseEntity<>(savedMapping, HttpStatus.OK);
    }

    @DeleteMapping("/la-students/{id}")
    public ResponseEntity<HttpStatus> deleteMapping(@PathVariable long id) {
        laStudentMappingRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{laId}/la-students/{studentId}")
    public ResponseEntity<HttpStatus> deleteStudentFromLa(@PathVariable long laId, @PathVariable long studentId) {
        List<LaStudentMapping> allStudentsForLa = laStudentMappingRepo.findByLaId(laId);

        Optional<LaStudentMapping> mappingToDelete = allStudentsForLa.stream()
                .filter(mapping -> mapping.getStudentId() == studentId)
                .findFirst();

        if (mappingToDelete.isPresent()) {
            laStudentMappingRepo.delete(mappingToDelete.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
