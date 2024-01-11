package com.pain_care.pain_care.controller;

import com.pain_care.pain_care.model.DiagnosticDTO;
import com.pain_care.pain_care.model.UserDTO;
import com.pain_care.pain_care.service.DiagnosticService;
import com.pain_care.pain_care.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.pain_care.pain_care.domain.UserDetailsinfo;

import java.security.Principal;


@Controller
public class HomeController {

    private final DiagnosticService diagnosticService;
    private final UserService userService;

    public HomeController(DiagnosticService diagnosticService, UserService userService) {
        this.diagnosticService = diagnosticService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(Principal principal, Model model) {

        if (principal != null) {
            String userName = principal.getName();
            UserDTO user = userService.get(userName);
            Integer userId = user.getId();
            String pic = user.getPic();
            String username = user.getName();
            DiagnosticDTO diagnosticDTO = diagnosticService.getLatestDiagnostic(userId);
            if (diagnosticDTO == null) {

                model.addAttribute("diagnosticResult", "Go !");

            } else {
                System.out.println(diagnosticDTO);
                model.addAttribute("diagnosticResult", diagnosticDTO.getResult());
            }

            model.addAttribute("username", username);
            model.addAttribute("userId", userId);
            model.addAttribute("picture", pic);
        }


        return "home/index";
    }

}
