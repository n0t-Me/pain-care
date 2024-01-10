package com.pain_care.pain_care.controller;

import com.pain_care.pain_care.domain.Feelings;
import com.pain_care.pain_care.domain.Locations;
import com.pain_care.pain_care.domain.MakePainWorse;
import com.pain_care.pain_care.domain.Symptome;
import com.pain_care.pain_care.domain.User;
import com.pain_care.pain_care.domain.UserDetailsinfo;
import com.pain_care.pain_care.model.PainRecordDTO;
import com.pain_care.pain_care.repos.UserRepository;
import com.pain_care.pain_care.service.PainRecordService;
import com.pain_care.pain_care.util.CustomCollectors;
import com.pain_care.pain_care.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/painRecords")
public class PainRecordController {

    private final PainRecordService painRecordService;
    private final UserRepository userRepository;

    public PainRecordController(final PainRecordService painRecordService,
                                final UserRepository userRepository) {
        this.painRecordService = painRecordService;
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
        model.addAttribute("painRecords", painRecordService.findAll());
        return "painRecord/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("painRecord") final PainRecordDTO painRecordDTO, @AuthenticationPrincipal UserDetailsinfo userDetails, Model model) {
        if (userDetails != null) {
            Integer userId = userDetails.getId();

            model.addAttribute("userId", userId);
            model.addAttribute("feelingsValues", Feelings.values());
            model.addAttribute("locationValues", Locations.values());
            model.addAttribute("symptomeValues", Symptome.values());
            model.addAttribute("makepainworseValues", MakePainWorse.values());

        }
        return "painRecord/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("painRecord") @Valid final PainRecordDTO painRecordDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "painRecord/add";
        }
        painRecordService.create(painRecordDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("painRecord.create.success"));
        return "redirect:/painRecords";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("painRecord", painRecordService.get(id));
        model.addAttribute("feelingsValues", Feelings.values());
        model.addAttribute("locationValues", Locations.values());
        model.addAttribute("symptomeValues", Symptome.values());
        model.addAttribute("makepainworseValues", MakePainWorse.values());
        return "painRecord/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
                       @ModelAttribute("painRecord") @Valid final PainRecordDTO painRecordDTO,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "painRecord/edit";
        }
        painRecordService.update(id, painRecordDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("painRecord.update.success"));
        return "redirect:/painRecords";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
                         final RedirectAttributes redirectAttributes) {
        painRecordService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("painRecord.delete.success"));
        return "redirect:/painRecords";
    }

}
