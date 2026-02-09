package com.example.demo.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.UserForm;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserRepository userRepository;
    private final UserService userService;

    public AdminUserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    public String list(@RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       Model model) {
        Page<User> userPage = userRepository.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(
            keyword == null ? "" : keyword,
            keyword == null ? "" : keyword,
            PageRequest.of(page, 10)
        );
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("page", userPage);
        model.addAttribute("keyword", keyword);
        return "admin/users";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "admin/user-form";
    }

    @PostMapping
    public String create(@Valid UserForm userForm, BindingResult bindingResult) {
        if (userRepository.existsByUsername(userForm.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "Tên đăng nhập đã tồn tại");
        }
        if (userRepository.existsByEmail(userForm.getEmail())) {
            bindingResult.rejectValue("email", "error.email", "Email đã được sử dụng");
        }
        if (bindingResult.hasErrors() || userForm.getPassword() == null || userForm.getPassword().isBlank()) {
            if (userForm.getPassword() == null || userForm.getPassword().isBlank()) {
                bindingResult.rejectValue("password", "error.password", "Mật khẩu không được để trống");
            }
            return "admin/user-form";
        }
        userService.createUser(userForm.getUsername(), userForm.getFullName(), userForm.getPhone(), userForm.getEmail(),
            userForm.getPassword(), userForm.getRole(), userForm.isEnabled());
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/admin/users";
        }
        UserForm form = new UserForm();
        form.setUsername(user.getUsername());
        form.setFullName(user.getFullName());
        form.setPhone(user.getPhone());
        form.setEmail(user.getEmail());
        form.setRole(user.getRole());
        form.setEnabled(user.isEnabled());
        model.addAttribute("userForm", form);
        model.addAttribute("userId", id);
        return "admin/user-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable String id, @Valid UserForm userForm,
                         BindingResult bindingResult, Principal principal, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/admin/users";
        }
        if (!user.getEmail().equalsIgnoreCase(userForm.getEmail())
            && userRepository.existsByEmail(userForm.getEmail())) {
            bindingResult.rejectValue("email", "error.email", "Email đã được sử dụng");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("userId", id);
            return "admin/user-form";
        }
        user.setFullName(userForm.getFullName());
        user.setPhone(userForm.getPhone());
        user.setEmail(userForm.getEmail());
        user.setRole(userForm.getRole());
        if (!user.getUsername().equals(principal.getName())) {
            user.setEnabled(userForm.isEnabled());
        }
        if (userForm.getPassword() != null && !userForm.getPassword().isBlank()) {
            user.setPassword(userService.encodePassword(userForm.getPassword()));
        }
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id, Principal principal) {
        userRepository.findById(id).ifPresent(user -> {
            if (!user.getUsername().equals(principal.getName())) {
                userRepository.deleteById(id);
            }
        });
        return "redirect:/admin/users";
    }
}
