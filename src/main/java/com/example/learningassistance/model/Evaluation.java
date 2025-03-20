package com.example.learningassistance.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Evaluations")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long submissionId;
    private long evaluatorId;
    private String feedback;
    private float score;
    private LocalDateTime evaluatedAt;

    public Evaluation() {
    }

    public Evaluation(long id, long submissionId, long evaluatorId, String feedback, float score, LocalDateTime evaluatedAt) {
        this.id = id;
        this.submissionId = submissionId;
        this.evaluatorId = evaluatorId;
        this.feedback = feedback;
        this.score = score;
        this.evaluatedAt = evaluatedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(long submissionId) {
        this.submissionId = submissionId;
    }

    public long getEvaluatorId() {
        return evaluatorId;
    }

    public void setEvaluatorId(long evaluatorId) {
        this.evaluatorId = evaluatorId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public LocalDateTime getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(LocalDateTime evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }
}
