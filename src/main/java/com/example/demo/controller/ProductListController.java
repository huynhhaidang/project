package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

@Controller
public class ProductListController {
    private final ProductService productService;

    public ProductListController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String list(@RequestParam(value = "category", required = false) String category,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       Model model,
                       HttpServletRequest request) {
        request.getSession();
        Page<Product> productPage = productService.findActive(category, keyword, PageRequest.of(page, 12));
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("page", productPage);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("keyword", keyword);
        return "products";
    }
}
