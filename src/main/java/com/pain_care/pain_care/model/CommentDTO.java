package com.pain_care.pain_care.model;

import jakarta.validation.constraints.NotNull;


public class CommentDTO {

    private Integer id;

    @NotNull
    private String comment;

    private Integer user;

    private Integer post;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(final Integer user) {
        this.user = user;
    }

    public Integer getPost() {
        return post;
    }

    public void setPost(final Integer post) {
        this.post = post;
    }

}
