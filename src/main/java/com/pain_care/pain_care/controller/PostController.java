package com.pain_care.pain_care.controller;

import com.pain_care.pain_care.domain.Comment;
import com.pain_care.pain_care.domain.Post;
import com.pain_care.pain_care.domain.User;
import com.pain_care.pain_care.model.CommentDTO;
import com.pain_care.pain_care.model.PostDTO;
import com.pain_care.pain_care.model.UserDTO;
import com.pain_care.pain_care.repos.UserRepository;
import com.pain_care.pain_care.service.CommentService;
import com.pain_care.pain_care.service.PostService;
import com.pain_care.pain_care.service.UserService;
import com.pain_care.pain_care.util.CustomCollectors;
import com.pain_care.pain_care.util.WebUtils;
import jakarta.validation.Valid;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;



@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CommentService commentService;

    public PostController(final PostService postService, UserService userService, final UserRepository userRepository, CommentService commentService) {
        this.postService = postService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.commentService = commentService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userValues", userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getName)));
    }

    @GetMapping
    public String list(Principal principal, final Model model) {
        //model.addAttribute("posts", postService.findAll());
        List<PostDTO> posts = postService.findAll();
        // Fetch user names for each post
        List<PostDTO> posts1 = postService.findAll();
        posts1 = posts1.stream()
            .sorted((post1, post2) -> post2.getLastUpdated().compareTo(post1.getLastUpdated()))
            .collect(Collectors.toList());

        Map<Integer, String> userNames = posts.stream()
                .filter(post -> post.getUser() != null)
                .collect(
                        toMap(
                                PostDTO::getUser,
                                PostService::getUserNameById,
                                (user1, user2) -> {
                                    return user1;
                                }
                        ));

        String username = principal.getName();
        UserDTO user = userService.get(username);

        posts1 = posts1.subList(0, Math.min(posts1.size(), 10));

        model.addAttribute("posts1", posts1);
        model.addAttribute("posts", posts);
        model.addAttribute("userNames", userNames);
        model.addAttribute("user", user);

        return "post/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("post") final PostDTO postDTO) {
        return "post/add";
    }

    @PostMapping("/add")
    public String add(
            Principal principal,
            @ModelAttribute("post") @Valid final PostDTO postDTO,
            @RequestParam("imageFile") MultipartFile imageFile,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "post/add";
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String encodedImage = Base64.getEncoder().encodeToString(imageFile.getBytes());
                postDTO.setImage(encodedImage);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        String username = principal.getName();
        UserDTO user = userService.get(username);
        postDTO.setUser(user.getId());
        postService.create(postDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("post.create.success"));
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String show(Principal principal,
                       @PathVariable final Integer id,
                       @ModelAttribute("comment") final CommentDTO commentDTO,
                       final Model model) {
        PostDTO postDTO = postService.get(id);
        UserDTO poster = userService.get(postDTO.getUser());
        String username = principal.getName();
        UserDTO user = userService.get(username);

        List<PostDTO> posts1 = postService.findAll();
        posts1 = posts1.stream()
            .sorted((post1, post2) -> post2.getLastUpdated().compareTo(post1.getLastUpdated()))
            .collect(Collectors.toList());
        /*
        Map<Integer, String> userNames = postDTO.getComments().stream()
                .filter(comment -> comment.getUser() != null)
                .collect(
                        toMap(
                                CommentService::getUserId,
                                CommentService::getUserNameById,
                                (user1, user2) -> {
                                    return user1;
                                }
                        ));*/
                        
        posts1 = posts1.subList(0, Math.min(posts1.size(), 10));

        model.addAttribute("posts1", posts1);
        model.addAttribute("post", postDTO);
        model.addAttribute("poster_username", poster.getName());
        model.addAttribute("user", user);
        model.addAttribute("comments", postDTO.getComments());
        //model.addAttribute("userNames", userNames);
        return "post/show";
    }

    @PostMapping("/{id}")
    public String show(Principal principal,
                       @PathVariable final Integer id,
                       @ModelAttribute("comment") @Valid final CommentDTO commentDTO, final BindingResult bindingResult,
                       final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "post/" + id.toString();
        }

        String username = principal.getName();
        UserDTO user = userService.get(username);
        commentDTO.setPost(id);
        commentDTO.setUser(user.getId());
        commentService.create(commentDTO);

        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("post.update.success"));
        return "redirect:/posts/" + id.toString();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("post", postService.get(id));
        return "post/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(
            Principal principal,
            @PathVariable final Integer id,
            @ModelAttribute("post") @Valid final PostDTO postDTO, final BindingResult bindingResult,
            @RequestParam("imageFile") MultipartFile imageFile,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "post/edit";
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String encodedImage = Base64.getEncoder().encodeToString(imageFile.getBytes());
                postDTO.setImage(encodedImage);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        String username = principal.getName();
        UserDTO user = userService.get(username);
        postDTO.setUser(user.getId());
        postService.update(id, postDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("post.update.success"));
        return "redirect:/posts";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
                         final RedirectAttributes redirectAttributes) {
        final String referencedWarning = postService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            postService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("post.delete.success"));
        }
        return "redirect:/posts";
    }

}
