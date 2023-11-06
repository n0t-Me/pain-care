package com.pain_care.pain_care.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "PainRecords")
@EntityListeners(AuditingEntityListener.class)
public class PainRecord {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer level;

    @Column(nullable = false, columnDefinition = "longtext")
    private String locations;

    @Column(nullable = false, columnDefinition = "longtext")
    private String symptoms;

    @Column(nullable = false, columnDefinition = "longtext")
    private String makePainWorse;

    @Column(nullable = false, columnDefinition = "longtext")
    private String feelings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

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

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(final OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
