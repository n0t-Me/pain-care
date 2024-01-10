package com.pain_care.pain_care.model;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.List;

import com.pain_care.pain_care.domain.Feelings;
import com.pain_care.pain_care.domain.Locations;
import com.pain_care.pain_care.domain.MakePainWorse;
import com.pain_care.pain_care.domain.Symptome;


public class PainRecordDTO {

    private Integer id;

    @NotNull
    private Integer level;

    @NotNull
    private List<Locations> locations;

    @NotNull
    private List<Symptome> symptoms;

    @NotNull
    private List<MakePainWorse> makePainWorse;

    @NotNull
    private List<Feelings> feelings;

    private Integer user;

    private OffsetDateTime dateCreated;

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

    public List<Locations> getLocations() {
        return locations;
    }

    public void setLocations(final List<Locations> locations) {
        this.locations = locations;
    }

    public List<Symptome> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(final List<Symptome> symptoms) {
        this.symptoms = symptoms;
    }

    public List<MakePainWorse> getMakePainWorse() {
        return makePainWorse;
    }

    public void setMakePainWorse(final List<MakePainWorse> makePainWorse) {
        this.makePainWorse = makePainWorse;
    }

    public List<Feelings> getFeelings() {
        return feelings;
    }

    public void setFeelings(final List<Feelings> feelings) {
        this.feelings = feelings;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(final Integer user) {
        this.user = user;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(final OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

}
