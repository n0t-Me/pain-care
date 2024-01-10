package com.pain_care.pain_care.domain;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "diagnostics")
@EntityListeners(AuditingEntityListener.class)
public class Diagnostic {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "user_id")
    private Integer userId;

    @Column(nullable = false)
    private String answers;

    @Column(nullable = false)
    private String result;

    @Column(nullable = false)
    private Float score;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    public List<Integer> getAnswersList() {
        return List.of(answers.split(","))
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
