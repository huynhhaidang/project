package com.example.demo.dto;

import java.util.List;

import com.example.demo.model.Product;

public class CategorySection {
    private String name;
    private List<Product> products;

    public CategorySection(String name, List<Product> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public List<Product> getProducts() {
        return products;
    }
}
