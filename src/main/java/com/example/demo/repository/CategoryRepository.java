package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.demo.model.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
    boolean existsByName(String name);
    Page<Category> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
