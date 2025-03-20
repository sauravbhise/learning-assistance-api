package com.example.learningassistance.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long assignmentId;
    private long studentId;
    private String file_url;
    private boolean evaluted;
    private LocalDateTime createdAt;

    public Submission() {
    }

    public Submission(long id, long assignmentId, long studentId, String file_url, boolean evaluted, LocalDateTime createdAt) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.file_url = file_url;
        this.evaluted = evaluted;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public boolean isEvaluted() {
        return evaluted;
    }

    public void setEvaluted(boolean evaluted) {
        this.evaluted = evaluted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
