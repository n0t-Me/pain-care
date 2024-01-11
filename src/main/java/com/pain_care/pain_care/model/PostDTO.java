package com.pain_care.pain_care.model;

import com.pain_care.pain_care.domain.Comment;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.Set;


public class PostDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    private String description;

    private String shortDesc;

    private String image;

    private Integer user;

    private Set<Comment> comments;

    private OffsetDateTime lastUpdated;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
        this.shortDesc = description.substring(0, Math.min(description.length(), 200));
    }

    public String getShortDesc() {
        return this.shortDesc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(final Integer user) {
        this.user = user;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
