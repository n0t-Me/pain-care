package com.pain_care.pain_care.controller;

import com.pain_care.pain_care.model.DiagnosticDTO;
import com.pain_care.pain_care.model.UserDTO;
import com.pain_care.pain_care.service.DiagnosticService;
import com.pain_care.pain_care.service.UserService;
import com.pain_care.pain_care.util.WebUtils;

import java.security.Principal;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/diagnostic")
public class DiagnosticController {

    private final DiagnosticService diagnosticService;
    private final UserService userService;

    public DiagnosticController(DiagnosticService diagnosticService, UserService userService) {
        this.diagnosticService = diagnosticService;
        this.userService = userService;
    }

    @GetMapping
    public String showDiagnosticForm(@ModelAttribute("diagnostics") @Valid final DiagnosticDTO diagnosticDTO, Model model) {

        Object[][] questionsBank = {
                {"When do you start your period ?", new String[]{
                        "Before 11 years old",
                        "Above 11 years old"
                }},
                {"Your menstrual cycle length average ?", new String[]{
                        "Less than 27 days",
                        "More than 27 days",
                        "Not sure"
                }},
                {"Do you have a familly history of endometriosis ?", new String[]{
                        "Yes",
                        "No"
                }},
                {"Did you give birth ?", new String[]{
                        "Yes",
                        "No"
                }},
                {"Do you have trouble getting pregnant ?", new String[]{
                        "Yes",
                        "No"
                }}
        };

        model.addAttribute("questionsBank", questionsBank);
        return "diagnostics/diagnostic";
    }

    @PostMapping
    public String submitDiagnosticForm(
            Principal principal,
            @ModelAttribute("diagnostics") @Valid final DiagnosticDTO diagnosticDTO,
            final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "diagnostics/diagnostic";
        }
        String username = principal.getName();
        UserDTO user = userService.get(username);
        try {
            diagnosticDTO.setUserId(user.getId());

            float calculatedScore = calculateScore(diagnosticDTO.getAnswers());
            diagnosticDTO.setScore(calculatedScore);


            String result = calculateResult(calculatedScore);
            diagnosticDTO.setResult(result);

            System.out.println(diagnosticDTO);


            diagnosticService.create(diagnosticDTO);


            redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("diagnostic.create.success"));


            return "redirect:/";
        } catch (Exception e) {

            e.printStackTrace();

            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, "An error occurred while processing the form.");

            return "redirect:/diagnostic";
        }
    }


    private float calculateScore(List<Integer> answers) {
        float totalScore = 0;


        for (int i = 0; i < answers.size(); i++) {
            int answerIndex = answers.get(i);

            switch (i) {
                case 0:

                    totalScore += (answerIndex == 0) ? 2.0 : 0.5;
                    break;
                case 1:

                    if (answerIndex == 0) {
                        totalScore += 1.0;
                    } else if (answerIndex == 1) {
                        totalScore += 1.5;
                    }

                    break;
                case 2:

                    totalScore += (answerIndex == 0) ? 3.0 : 0.5;
                    break;
                case 3:

                    totalScore += (answerIndex == 0) ? 1.0 : 0.5;
                    break;
                case 4:

                    totalScore += (answerIndex == 0) ? 2.0 : 0.5;
                    break;

            }
        }

        return totalScore;
    }


    private String calculateResult(float score) {


        if (score < 4) {
            return "Low";
        } else if (score < 7) {
            return "Medium";
        } else {
            return "High";
        }
    }
}