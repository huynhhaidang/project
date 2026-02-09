package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByCategoryName(String categoryName);
    Page<Product> findByNameContainingIgnoreCaseAndActiveTrue(String keyword, Pageable pageable);
    Page<Product> findByActiveTrue(Pageable pageable);
    Page<Product> findByCategoryNameAndActiveTrue(String categoryName, Pageable pageable);
    Page<Product> findByCategoryNameAndNameContainingIgnoreCaseAndActiveTrue(String categoryName, String keyword, Pageable pageable);
    List<Product> findByCategoryNameAndActiveTrue(String categoryName);
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    long countByCategoryName(String categoryName);
}
