package com.pain_care.pain_care.controller;

import com.pain_care.pain_care.model.DiagnosticDTO;
import com.pain_care.pain_care.service.DiagnosticService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pain_care.pain_care.domain.UserDetailsinfo;


@Controller
public class HomeController {

    private final DiagnosticService diagnosticService;

    public HomeController(DiagnosticService diagnosticService) {
        this.diagnosticService = diagnosticService;
    }

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserDetailsinfo userDetails,Model model) {

            if (userDetails != null) {
                String username = userDetails.getName();
                Integer userId = userDetails.getId();
                String pic = userDetails.getPic();
                DiagnosticDTO diagnosticDTO = diagnosticService.getLatestDiagnostic(userId);
                                 if (diagnosticDTO== null) {

                                model.addAttribute("diagnosticResult", "vas-y");

                      }else{
                System.out.println(diagnosticDTO);
                                model.addAttribute("diagnosticResult", diagnosticDTO.getResult());}

                model.addAttribute("username", username);
                model.addAttribute("userId", userId);
                model.addAttribute("picture", pic);
            }


        return "home/index";
    }

}
