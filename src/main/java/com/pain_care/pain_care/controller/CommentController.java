package com.pain_care.pain_care.controller;

import com.pain_care.pain_care.domain.Post;
import com.pain_care.pain_care.domain.User;
import com.pain_care.pain_care.model.CommentDTO;
import com.pain_care.pain_care.repos.PostRepository;
import com.pain_care.pain_care.repos.UserRepository;
import com.pain_care.pain_care.service.CommentService;
import com.pain_care.pain_care.util.CustomCollectors;
import com.pain_care.pain_care.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentController(final CommentService commentService,
                             final UserRepository userRepository, final PostRepository postRepository) {
        this.commentService = commentService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userValues", userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getName)));
        model.addAttribute("postValues", postRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Post::getId, Post::getTitle)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("comments", commentService.findAll());
        return "comment/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("comment") final CommentDTO commentDTO) {
        return "comment/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("comment") @Valid final CommentDTO commentDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "comment/add";
        }
        commentService.create(commentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("comment.create.success"));
        return "redirect:/comments";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("comment", commentService.get(id));
        return "comment/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
                       @ModelAttribute("comment") @Valid final CommentDTO commentDTO,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "comment/edit";
        }
        commentService.update(id, commentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("comment.update.success"));
        return "redirect:/comments";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
                         final RedirectAttributes redirectAttributes) {
        commentService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("comment.delete.success"));
        return "redirect:/comments";
    }

}
