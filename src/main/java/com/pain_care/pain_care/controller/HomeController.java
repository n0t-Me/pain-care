package com.pain_care.pain_care.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pain_care.pain_care.domain.UserDetailsinfo;


@Controller
public class HomeController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetailsinfo userDetails,Model model,@RequestParam(name = "result", required = false) String diagnosticResult) {

            if (userDetails != null) {
                String username = userDetails.getName();
                Integer userId = userDetails.getId();
                String pic = userDetails.getPic();

                model.addAttribute("username", username);
                model.addAttribute("userId", userId);
                model.addAttribute("picture", pic);
                model.addAttribute("diagnosticResult", diagnosticResult);
            }


        return "home/index";
    }

}
