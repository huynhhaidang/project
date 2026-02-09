package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.demo.dto.CategoryForm;
import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;
    private final ProductService productService;

    public AdminCategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    public String list(@RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       Model model) {
        Page<Category> categoryPage = categoryService.search(keyword, PageRequest.of(page, 10));
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("page", categoryPage);
        model.addAttribute("keyword", keyword);
        return "admin/categories";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "admin/category-form";
    }

    @PostMapping
    public String create(@Valid CategoryForm categoryForm, BindingResult bindingResult, Model model) {
        if (categoryService.existsByName(categoryForm.getName())) {
            bindingResult.rejectValue("name", "error.name", "Danh mục đã tồn tại");
        }
        if (bindingResult.hasErrors()) {
            return "admin/category-form";
        }
        Category category = new Category(categoryForm.getName(), categoryForm.getDescription());
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        Category category = categoryService.findById(id).orElse(null);
        if (category == null) {
            return "redirect:/admin/categories";
        }
        CategoryForm form = new CategoryForm();
        form.setName(category.getName());
        form.setDescription(category.getDescription());
        model.addAttribute("categoryForm", form);
        model.addAttribute("categoryId", id);
        return "admin/category-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable String id, @Valid CategoryForm categoryForm,
                         BindingResult bindingResult, Model model) {
        Category category = categoryService.findById(id).orElse(null);
        if (category == null) {
            return "redirect:/admin/categories";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryId", id);
            return "admin/category-form";
        }
        category.setName(categoryForm.getName());
        category.setDescription(categoryForm.getDescription());
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        Category category = categoryService.findById(id).orElse(null);
        if (category != null && productService.countByCategoryName(category.getName()) == 0) {
            categoryService.deleteById(id);
            return "redirect:/admin/categories";
        }
        return "redirect:/admin/categories?error=locked";
    }
}
