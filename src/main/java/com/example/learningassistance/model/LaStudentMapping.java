package com.example.learningassistance.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "LaStudentMapping")
public class LaStudentMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long laId;
    private long studentId;
    private LocalDateTime assignedAt;

    public LaStudentMapping() {
    }

    public LaStudentMapping(long id, long laId, long studentId, LocalDateTime assignedAt) {
        this.id = id;
        this.laId = laId;
        this.studentId = studentId;
        this.assignedAt = assignedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLaId() {
        return laId;
    }

    public void setLaId(long laId) {
        this.laId = laId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
}
