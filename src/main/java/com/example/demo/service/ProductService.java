package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByCategoryName(String categoryName) {
        return productRepository.findByCategoryName(categoryName);
    }

    public List<Product> findActiveByCategory(String categoryName, int limit) {
        return productRepository.findByCategoryNameAndActiveTrue(categoryName)
            .stream()
            .limit(limit)
            .toList();
    }

    public Page<Product> findActive(String categoryName, String keyword, Pageable pageable) {
        boolean hasCategory = categoryName != null && !categoryName.isBlank();
        boolean hasKeyword = keyword != null && !keyword.isBlank();
        if (hasCategory && hasKeyword) {
            return productRepository.findByCategoryNameAndNameContainingIgnoreCaseAndActiveTrue(categoryName, keyword, pageable);
        }
        if (hasCategory) {
            return productRepository.findByCategoryNameAndActiveTrue(categoryName, pageable);
        }
        if (hasKeyword) {
            return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(keyword, pageable);
        }
        return productRepository.findByActiveTrue(pageable);
    }

    public Page<Product> searchAll(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return productRepository.findAll(pageable);
        }
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(String id) {
        productRepository.deleteById(id);
    }

    public long countByCategoryName(String categoryName) {
        return productRepository.countByCategoryName(categoryName);
    }
}
