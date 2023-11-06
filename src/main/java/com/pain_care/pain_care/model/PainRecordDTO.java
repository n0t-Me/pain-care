package com.pain_care.pain_care.model;

import jakarta.validation.constraints.NotNull;


public class PainRecordDTO {

    private Integer id;

    @NotNull
    private Integer level;

    @NotNull
    private String locations;

    @NotNull
    private String symptoms;

    @NotNull
    private String makePainWorse;

    @NotNull
    private String feelings;

    private Integer user;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(final Integer level) {
        this.level = level;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(final String locations) {
        this.locations = locations;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(final String symptoms) {
        this.symptoms = symptoms;
    }

    public String getMakePainWorse() {
        return makePainWorse;
    }

    public void setMakePainWorse(final String makePainWorse) {
        this.makePainWorse = makePainWorse;
    }

    public String getFeelings() {
        return feelings;
    }

    public void setFeelings(final String feelings) {
        this.feelings = feelings;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(final Integer user) {
        this.user = user;
    }

}
