package com.pain_care.pain_care.controller;

import com.pain_care.pain_care.domain.User;
import com.pain_care.pain_care.model.DiagnosticDTO;
import com.pain_care.pain_care.model.UserDTO;
import com.pain_care.pain_care.repos.UserRepository;
import com.pain_care.pain_care.service.DiagnosticService;
import com.pain_care.pain_care.service.UserService;
import com.pain_care.pain_care.service.UserinfoService;
import com.pain_care.pain_care.util.WebUtils;

import java.security.Principal;
import java.security.Security;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import jakarta.validation.Valid;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        // Replace with your logic to retrieve questionsBank data
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
                {"Do you have a family history of endometriosis ?", new String[]{
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

        // Set the result (replace this with your actual result calculation logic)
        String result = calculateResult(calculatedScore);
        diagnosticDTO.setResult(result);

        System.out.println(diagnosticDTO);

        // Save the diagnostic
        diagnosticService.create(diagnosticDTO);

        // Add a flash attribute for success message if needed
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("diagnostic.create.success"));

        // Redirect to home page
        return "redirect:/";
    } catch (Exception e) {
        // Log the exception for troubleshooting
        e.printStackTrace();  // Replace with a proper logging mechanism
        // Add a flash attribute for error message
        redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, "An error occurred while processing the form.");
        // Redirect to the diagnostic form page
        return "redirect:/diagnostic";
    }
}

    // Your actual score calculation logic
    private float calculateScore(List<Integer> answers) {
        float totalScore = 0;

        // Map the selected answers to their corresponding scores based on the provided scoring logic
        for (int i = 0; i < answers.size(); i++) {
            int answerIndex = answers.get(i);

            switch (i) {
                case 0:
                    // "When do you start your period ?"
                    totalScore += (answerIndex == 0) ? 2.0 : 0.5;
                    break;
                case 1:
                    // "Your menstrual cycle length average ?"
                    if (answerIndex == 0) {
                        totalScore += 1.0;
                    } else if (answerIndex == 1) {
                        totalScore += 1.5;
                    }
                    // No score for "Not sure"
                    break;
                case 2:
                    // "Do you have a family history of endometriosis ?"
                    totalScore += (answerIndex == 0) ? 3.0 : 0.5;
                    break;
                case 3:
                    // "Did you give birth ?"
                    totalScore += (answerIndex == 0) ? 1.0 : 0.5;
                    break;
                case 4:
                    // "Do you have trouble getting pregnant ?"
                    totalScore += (answerIndex == 0) ? 2.0 : 0.5;
                    break;
                // Add more cases if you have additional questions
            }
        }

        return totalScore;
    }

    // Your actual result calculation logic
    private String calculateResult(float score) {
        // Replace this with your logic to determine the result based on the score
        // Example: Low, Medium, High based on a threshold
        if (score < 4) {
            return "Low";
        } else if (score < 7) {
            return "Medium";
        } else {
            return "High";
        }
    }
}