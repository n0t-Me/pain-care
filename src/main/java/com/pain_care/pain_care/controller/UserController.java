package com.pain_care.pain_care.controller;

import com.pain_care.pain_care.model.UserDTO;
import com.pain_care.pain_care.service.UserService;
import com.pain_care.pain_care.util.WebUtils;
import jakarta.validation.Valid;

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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("user") final UserDTO userDTO) {
        return "user/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("user") @Valid final UserDTO userDTO,
                      @RequestParam("imageFile") MultipartFile imageFile,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") && userService.emailExists(userDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.user.email");
        }
        if (bindingResult.hasErrors()) {
            return "user/add";
        }

        // Convert MultipartFile to Base64-encoded string and set it in PostDTO
       if (imageFile != null && !imageFile.isEmpty()) {
        try {
            String encodedImage = Base64.getEncoder().encodeToString(imageFile.getBytes());
            userDTO.setPic(encodedImage);
        } catch (IOException e) {
            // Handle exception (e.g., log error, show user-friendly message)
            e.printStackTrace();
        }
    }
        userService.create(userDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.create.success"));
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("user", userService.get(id));
        return "user/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
                       @RequestParam("imageFile") MultipartFile imageFile,
            @ModelAttribute("user") @Valid final UserDTO userDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        final UserDTO currentUserDTO = userService.get(id);
        if (!bindingResult.hasFieldErrors("email") &&
                !userDTO.getEmail().equalsIgnoreCase(currentUserDTO.getEmail()) &&
                userService.emailExists(userDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.user.email");
        }
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }
        // Convert MultipartFile to Base64-encoded string and set it in PostDTO
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String encodedImage = Base64.getEncoder().encodeToString(imageFile.getBytes());
                userDTO.setPic(encodedImage);
            } catch (IOException e) {
                // Handle exception (e.g., log error, show user-friendly message)
                e.printStackTrace();
            }
        }
        userService.update(id, userDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.update.success"));
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = userService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            userService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("user.delete.success"));
        }
        return "redirect:/users";
    }
    @GetMapping("/register")
    public String register(@ModelAttribute("user") final UserDTO userDTO) {
        return "user/register";
    }

    @PostMapping("/register")
public String register(@ModelAttribute("user") @Valid final UserDTO userDTO,
                        @RequestParam("imageFile") MultipartFile imageFile,
        final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
        return "user/register";
    }

    // Check if the email already exists
    if (userService.emailExists(userDTO.getEmail())) {
        bindingResult.rejectValue("email", "Exists.user.email");
        return "user/register";
    }
    // Convert MultipartFile to Base64-encoded string and set it in PostDTO
       if (imageFile != null && !imageFile.isEmpty()) {
        try {
            String encodedImage = Base64.getEncoder().encodeToString(imageFile.getBytes());
            userDTO.setPic(encodedImage);
        } catch (IOException e) {
            // Handle exception (e.g., log error, show user-friendly message)
            e.printStackTrace();
        }
    }
    // Other validation and registration logic
    userService.create(userDTO);

    redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.register.success"));
    return "redirect:/users";
}

@GetMapping("/login")
    public String login(@ModelAttribute("user") final UserDTO userDTO) {
        return "user/login";
    }
 @PostMapping("/login")
    public String processLogin(@ModelAttribute("user") final UserDTO userDTO) {
            // Handle authentication logic here
            // If using Spring Security, this method may not contain explicit authentication logic
            return "home/index";
        }    
}

