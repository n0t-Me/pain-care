package com.pain_care.pain_care.service;

import com.pain_care.pain_care.domain.Comment;
import com.pain_care.pain_care.domain.Post;
import com.pain_care.pain_care.domain.User;
import com.pain_care.pain_care.model.PostDTO;
import com.pain_care.pain_care.repos.CommentRepository;
import com.pain_care.pain_care.repos.PostRepository;
import com.pain_care.pain_care.repos.UserRepository;
import com.pain_care.pain_care.util.NotFoundException;
import com.pain_care.pain_care.util.WebUtils;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PostService {

    private static UserRepository userRepository = null;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostService(final PostRepository postRepository, final UserRepository userRepository,
                       final CommentRepository commentRepository) {
        this.postRepository = postRepository;
        PostService.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public static String getUserNameById(PostDTO post) {
        int userId = post.getUser();
        return userRepository.findById(userId)
                .map(User::getName)
                .orElse(null); // or any default value you want to use
    }

    public List<PostDTO> findAll() {
        final List<Post> posts = postRepository.findAll(Sort.by("id"));
        return posts.stream()
                .map(post -> mapToDTO(post, new PostDTO()))
                .toList();
    }

    public PostDTO get(final Integer id) {
        return postRepository.findById(id)
                .map(post -> mapToDTO(post, new PostDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final PostDTO postDTO) {
        final Post post = new Post();
        mapToEntity(postDTO, post);
        return postRepository.save(post).getId();
    }

    public void update(final Integer id, final PostDTO postDTO) {
        final Post post = postRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(postDTO, post);
        postRepository.save(post);
    }

    public void delete(final Integer id) {
        postRepository.deleteById(id);
    }

    private PostDTO mapToDTO(final Post post, final PostDTO postDTO) {
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setDescription(post.getDescription());
        postDTO.setImage(post.getImage());
        postDTO.setUser(post.getUser() == null ? null : post.getUser().getId());
        postDTO.setComments(post.getComments());
        postDTO.setLastUpdated(post.getLastUpdated());
        return postDTO;
    }

    private Post mapToEntity(final PostDTO postDTO, final Post post) {
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setImage(postDTO.getImage());
        final User user = postDTO.getUser() == null ? null : userRepository.findById(postDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        post.setUser(user);
        post.setComments(postDTO.getComments());
        post.setLastUpdated(postDTO.getLastUpdated());
        return post;
    }

    public String getReferencedWarning(final Integer id) {
        final Post post = postRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Comment postComment = commentRepository.findFirstByPost(post);
        if (postComment != null) {
            return WebUtils.getMessage("post.comment.post.referenced", postComment.getId());
        }
        return null;
    }

}
