package com.example.learningassistance.repo;

import com.example.learningassistance.model.LaStudentMapping;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaStudentMappingRepo extends JpaRepository<LaStudentMapping, Long> {

    List<LaStudentMapping> findByLaId(long laId);
    LaStudentMapping findByStudentId(long studentId);

    @Transactional
    void deleteByLaId(long laId);
    @Transactional
    void deleteByStudentId(long studentId);
}
