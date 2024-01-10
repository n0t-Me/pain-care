package com.pain_care.pain_care.service;

import com.pain_care.pain_care.domain.Comment;
import com.pain_care.pain_care.domain.Post;
import com.pain_care.pain_care.domain.User;
import com.pain_care.pain_care.model.CommentDTO;
import com.pain_care.pain_care.model.PostDTO;
import com.pain_care.pain_care.repos.CommentRepository;
import com.pain_care.pain_care.repos.PostRepository;
import com.pain_care.pain_care.repos.UserRepository;
import com.pain_care.pain_care.util.NotFoundException;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(final CommentRepository commentRepository,
                          final UserRepository userRepository, final PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public List<CommentDTO> findAll() {
        final List<Comment> comments = commentRepository.findAll(Sort.by("id"));
        return comments.stream()
                .map(comment -> mapToDTO(comment, new CommentDTO()))
                .toList();
    }

    public CommentDTO get(final Integer id) {
        return commentRepository.findById(id)
                .map(comment -> mapToDTO(comment, new CommentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final CommentDTO commentDTO) {
        final Comment comment = new Comment();
        mapToEntity(commentDTO, comment);
        return commentRepository.save(comment).getId();
    }

    public void update(final Integer id, final CommentDTO commentDTO) {
        final Comment comment = commentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(commentDTO, comment);
        commentRepository.save(comment);
    }

    public void delete(final Integer id) {
        commentRepository.deleteById(id);
    }

    private CommentDTO mapToDTO(final Comment comment, final CommentDTO commentDTO) {
        commentDTO.setId(comment.getId());
        commentDTO.setComment(comment.getComment());
        commentDTO.setUser(comment.getUser() == null ? null : comment.getUser().getId());
        commentDTO.setPost(comment.getPost() == null ? null : comment.getPost().getId());
        return commentDTO;
    }

    private Comment mapToEntity(final CommentDTO commentDTO, final Comment comment) {
        comment.setComment(commentDTO.getComment());
        final User user = commentDTO.getUser() == null ? null : userRepository.findById(commentDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        comment.setUser(user);
        final Post post = commentDTO.getPost() == null ? null : postRepository.findById(commentDTO.getPost())
                .orElseThrow(() -> new NotFoundException("post not found"));
        comment.setPost(post);
        return comment;
    }

    public static String getUserNameById(Comment post) {
        return post.getUser().getName();
    }

    public static Integer getUserId(Comment comment) {
        return comment.getUser().getId();
    }
}
