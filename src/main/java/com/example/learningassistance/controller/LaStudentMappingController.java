package com.example.learningassistance.controller;

import com.example.learningassistance.model.LaStudentMapping;
import com.example.learningassistance.repo.LaStudentMappingRepo;
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

    @GetMapping("/la/{laId}/students")
    public ResponseEntity<List<LaStudentMapping>> getMappingByLaId(@PathVariable long laId) {
        try {
            List<LaStudentMapping> mappingList = new ArrayList<>();
            laStudentMappingRepo.findByLaId(laId).forEach(mappingList::add);

            if (mappingList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(mappingList, HttpStatus.OK);

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
}
