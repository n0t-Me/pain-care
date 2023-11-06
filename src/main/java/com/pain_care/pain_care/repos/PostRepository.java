package com.pain_care.pain_care.repos;

import com.pain_care.pain_care.domain.Post;
import com.pain_care.pain_care.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Integer> {

    Post findFirstByUser(User user);

}
