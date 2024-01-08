package com.pain_care.pain_care.model;

import java.util.List;

public class DiagnosticDTO {

    private Integer id;
    private Integer userId;
    private List<Integer> answers;
    private Float score;
    private String result;


    public DiagnosticDTO() {
    }

    public DiagnosticDTO(Integer id, Integer userId, List<Integer> answers, Float score, String result) {
        this.id = id;
        this.userId = userId;
        this.answers = answers;
        this.score = score;
        this.result = result;
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

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> answers) {
        this.answers = answers;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "DiagnosticDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", answers=" + answers +
                ", score=" + score +
                ", result='" + result + '\'' +
                '}';
    }
}
