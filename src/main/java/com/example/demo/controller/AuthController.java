package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest, BindingResult bindingResult, Model model) {
        if (userService.usernameExists(registerRequest.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "Tên đăng nhập đã tồn tại");
        }
        if (registerRequest.getEmail() != null && userService.emailExists(registerRequest.getEmail())) {
            bindingResult.rejectValue("email", "error.email", "Email đã được sử dụng");
        }
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", "Mật khẩu nhập lại không khớp");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.register(registerRequest);
        model.addAttribute("successMessage", "Đăng ký thành công, vui lòng đăng nhập");
        return "login";
    }
}
