package com.pain_care.pain_care.repos;

import com.pain_care.pain_care.domain.Comment;
import com.pain_care.pain_care.domain.Post;
import com.pain_care.pain_care.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Comment findFirstByUser(User user);

    Comment findFirstByPost(Post post);

}
