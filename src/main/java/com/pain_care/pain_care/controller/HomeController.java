package com.pain_care.pain_care.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pain_care.pain_care.domain.UserDetailsinfo;


@Controller
public class HomeController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetailsinfo userDetails,Model model) {

            if (userDetails != null) {
                String username = userDetails.getName();
                model.addAttribute("username", username);
            }


        return "home/index";
    }

}
