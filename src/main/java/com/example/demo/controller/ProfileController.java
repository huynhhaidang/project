package com.example.demo.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.dto.UserProfileForm;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import jakarta.validation.Valid;

@Controller
public class ProfileController {
    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        UserProfileForm form = new UserProfileForm();
        form.setFullName(user.getFullName());
        form.setPhone(user.getPhone());
        form.setEmail(user.getEmail());
        form.setShippingAddress(user.getShippingAddress());
        model.addAttribute("profileForm", form);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("profileForm") UserProfileForm form,
                                BindingResult bindingResult,
                                Principal principal,
                                Model model) {
        if (bindingResult.hasErrors()) {
            return "profile";
        }

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        user.setFullName(form.getFullName());
        user.setPhone(form.getPhone());
        user.setEmail(form.getEmail());
        user.setShippingAddress(form.getShippingAddress());
        userRepository.save(user);
        model.addAttribute("successMessage", "Cập nhật thông tin thành công");
        return "profile";
    }
}
