package com.pain_care.pain_care.service;

import com.pain_care.pain_care.domain.Comment;
import com.pain_care.pain_care.domain.PainRecord;
import com.pain_care.pain_care.domain.Post;
import com.pain_care.pain_care.domain.User;
import com.pain_care.pain_care.model.UserDTO;
import com.pain_care.pain_care.repos.CommentRepository;
import com.pain_care.pain_care.repos.PainRecordRepository;
import com.pain_care.pain_care.repos.PostRepository;
import com.pain_care.pain_care.repos.UserRepository;
import com.pain_care.pain_care.util.NotFoundException;
import com.pain_care.pain_care.util.WebUtils;

import java.util.List;


import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PainRecordRepository painRecordRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(final UserRepository userRepository, final PostRepository postRepository,
                       final CommentRepository commentRepository,
                       final PainRecordRepository painRecordRepository, final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.painRecordRepository = painRecordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Integer id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UserDTO get(final String email) {
        return userRepository.findByEmail(email)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public void update(final Integer id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void delete(final Integer id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setBday(user.getBday());
        userDTO.setPic(user.getPic());
        userDTO.setLanguage(user.getLanguage());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setBday(userDTO.getBday());
        user.setPic(userDTO.getPic());
        user.setLanguage(userDTO.getLanguage());
        return user;
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public String getReferencedWarning(final Integer id) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Post userPost = postRepository.findFirstByUser(user);
        if (userPost != null) {
            return WebUtils.getMessage("user.post.user.referenced", userPost.getId());
        }
        final Comment userComment = commentRepository.findFirstByUser(user);
        if (userComment != null) {
            return WebUtils.getMessage("user.comment.user.referenced", userComment.getId());
        }
        final PainRecord userPainRecord = painRecordRepository.findFirstByUser(user);
        if (userPainRecord != null) {
            return WebUtils.getMessage("user.painRecord.user.referenced", userPainRecord.getId());
        }
        return null;
    }

}
