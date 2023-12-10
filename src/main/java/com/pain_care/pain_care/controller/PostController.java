package com.pain_care.pain_care.controller;

import com.pain_care.pain_care.domain.User;
import com.pain_care.pain_care.model.PostDTO;
import com.pain_care.pain_care.repos.UserRepository;
import com.pain_care.pain_care.service.PostService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException; 
import java.util.Base64;


@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    public PostController(final PostService postService, final UserRepository userRepository) {
        this.postService = postService;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userValues", userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("posts", postService.findAll());
        return "post/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("post") final PostDTO postDTO) {
        return "post/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("post") @Valid final PostDTO postDTO,
                      @RequestParam("imageFile") MultipartFile imageFile,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "post/add";
        }
       // Convert MultipartFile to Base64-encoded string and set it in PostDTO
       if (imageFile != null && !imageFile.isEmpty()) {
        try {
            String encodedImage = Base64.getEncoder().encodeToString(imageFile.getBytes());
            postDTO.setImage(encodedImage);
        } catch (IOException e) {
            // Handle exception (e.g., log error, show user-friendly message)
            e.printStackTrace();
        }
    }
        postService.create(postDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("post.create.success"));
        return "redirect:/posts";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("post", postService.get(id));
        return "post/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("post") @Valid final PostDTO postDTO, final BindingResult bindingResult,
            @RequestParam("imageFile") MultipartFile imageFile,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "post/edit";
        }
        // Convert MultipartFile to Base64-encoded string and set it in PostDTO
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String encodedImage = Base64.getEncoder().encodeToString(imageFile.getBytes());
                postDTO.setImage(encodedImage);
            } catch (IOException e) {
                // Handle exception (e.g., log error, show user-friendly message)
                e.printStackTrace();
            }
        }
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
