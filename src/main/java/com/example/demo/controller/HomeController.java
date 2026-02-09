package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.CategorySection;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;

@Controller
public class HomeController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public HomeController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String home(@RequestParam(value = "category", required = false) String category,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       Model model,
                       HttpServletRequest request) {
        // Ensure session exists before template renders POST forms (CSRF token)
        request.getSession();

        Page<Product> productPage = productService.findActive(category, keyword, PageRequest.of(page, 9));
        List<Product> products = productPage.getContent();

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", productPage);

        List<Product> heroProducts = new ArrayList<>();
        for (Product p : products) {
            if (heroProducts.size() >= 3) break;
            heroProducts.add(p);
        }

        List<CategorySection> sections = new ArrayList<>();
        categoryService.findAll().forEach(c -> {
            List<Product> list = productService.findActiveByCategory(c.getName(), 5);
            if (!list.isEmpty()) {
                sections.add(new CategorySection(c.getName(), list));
            }
        });

        model.addAttribute("heroProducts", heroProducts);
        model.addAttribute("sections", sections);
        model.addAttribute("collectionSections", sections.stream().limit(2).toList());
        return "index";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable String id, Model model) {
        Product product = productService.findById(id).orElse(null);
        if (product == null || !product.isActive()) {
            return "redirect:/";
        }
        model.addAttribute("product", product);
        return "product-detail";
    }
}
