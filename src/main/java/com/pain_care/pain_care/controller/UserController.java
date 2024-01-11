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
import java.security.Principal;
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
                      @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") && userService.emailExists(userDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.user.email");
        }
        if (bindingResult.hasErrors()) {
            return "user/add";
        }


        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String encodedImage = Base64.getEncoder().encodeToString(imageFile.getBytes());
                userDTO.setPic(encodedImage);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        userService.create(userDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.create.success"));
        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String edit(Principal principal, final Model model) {
        String username = principal.getName();
        UserDTO user = userService.get(username);
        model.addAttribute("user", user);
        return "user/edit";
    }

    @PostMapping("/edit")
    public String edit(Principal principal,
                       @RequestParam("imageFile") MultipartFile imageFile,
                       @ModelAttribute("user") @Valid final UserDTO userDTO, final BindingResult bindingResult,
                       final RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        UserDTO currentUserDTO = userService.get(username);
        if (!bindingResult.hasFieldErrors("email") &&
                (!userDTO.getEmail().equalsIgnoreCase(currentUserDTO.getEmail()) &&
                userService.emailExists(userDTO.getEmail()))) {
            System.out.println("EMAIL ERR");
            bindingResult.rejectValue("email", "Exists.user.email");
        }
        if (bindingResult.hasErrors()) {
            if (!(bindingResult.getErrorCount() == 1 &&
            bindingResult.hasFieldErrors("password"))) {
                System.out.println(bindingResult.getAllErrors());
                return "user/edit";
            }
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String encodedImage = Base64.getEncoder().encodeToString(imageFile.getBytes());
                userDTO.setPic(encodedImage);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        System.out.println(userDTO);
        currentUserDTO.setBday(userDTO.getBday());
        currentUserDTO.setEmail(userDTO.getEmail());
        currentUserDTO.setLanguage(userDTO.getLanguage());
        currentUserDTO.setName(userDTO.getName());
        System.out.println(currentUserDTO);
        userService.update(currentUserDTO.getId(), currentUserDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.update.success"));
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(Principal principal,
                         final RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        UserDTO currentUserDTO = userService.get(username);
        final String referencedWarning = userService.getReferencedWarning(currentUserDTO.getId());
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            userService.delete(currentUserDTO.getId());
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
                           @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                           final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") && userService.emailExists(userDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.user.email");
        }
        if (bindingResult.hasErrors()) {
            return "user/register";
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String encodedImage = Base64.getEncoder().encodeToString(imageFile.getBytes());
                userDTO.setPic(encodedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userService.create(userDTO);

        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.register.success"));
        return "user/login";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("user") final UserDTO userDTO) {
        return "user/login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("user") final UserDTO userDTO) {
        return "redirect:/";
    }
}

