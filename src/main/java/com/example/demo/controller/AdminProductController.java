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

import com.example.demo.dto.ProductForm;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import com.example.demo.repository.OrderRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderRepository orderRepository;

    public AdminProductController(ProductService productService, CategoryService categoryService,
                                  OrderRepository orderRepository) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public String list(@RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       Model model) {
        Page<Product> productPage = productService.searchAll(keyword, PageRequest.of(page, 10));
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("page", productPage);
        model.addAttribute("keyword", keyword);
        return "admin/products";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("productForm", new ProductForm());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product-form";
    }

    @PostMapping
    public String create(@Valid ProductForm productForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/product-form";
        }
        Product product = mapToProduct(productForm, new Product());
        productService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        Product product = productService.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/admin/products";
        }
        ProductForm form = mapToForm(product);
        model.addAttribute("productForm", form);
        model.addAttribute("productId", id);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/product-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable String id, @Valid ProductForm productForm, BindingResult bindingResult, Model model) {
        Product product = productService.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/admin/products";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("productId", id);
            return "admin/product-form";
        }
        mapToProduct(productForm, product);
        productService.save(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        if (!orderRepository.existsByItemsProductId(id)) {
            productService.deleteById(id);
            return "redirect:/admin/products";
        }
        return "redirect:/admin/products?error=locked";
    }

    private Product mapToProduct(ProductForm form, Product product) {
        product.setName(form.getName());
        product.setBrand(form.getBrand());
        product.setCategoryName(form.getCategoryName());
        product.setPrice(form.getPrice());
        product.setDescription(form.getDescription());
        product.setImageUrl(form.getImageUrl());
        product.setStock(form.getStock());
        product.setActive(form.isActive());
        return product;
    }

    private ProductForm mapToForm(Product product) {
        ProductForm form = new ProductForm();
        form.setName(product.getName());
        form.setBrand(product.getBrand());
        form.setCategoryName(product.getCategoryName());
        form.setPrice(product.getPrice());
        form.setDescription(product.getDescription());
        form.setImageUrl(product.getImageUrl());
        form.setStock(product.getStock());
        form.setActive(product.isActive());
        return form;
    }
}
