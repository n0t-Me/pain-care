package com.pain_care.pain_care.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class PostDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    private String description;

    @Size(max = 255)
    private String image;

    private Integer user;

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

}
