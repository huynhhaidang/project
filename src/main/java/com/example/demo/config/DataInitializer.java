package com.example.demo.config;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(ProductRepository productRepository,
                                      CategoryRepository categoryRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            if (categoryRepository.count() == 0) {
                categoryRepository.saveAll(Arrays.asList(
                    new Category("Điện thoại", "Các dòng smartphone mới nhất"),
                    new Category("Laptop", "Laptop cho học tập và làm việc"),
                    new Category("Phụ kiện", "Tai nghe, sạc, chuột, bàn phím")
                ));
            }

            if (productRepository.count() == 0) {
                List<Category> categories = categoryRepository.findAll();
                String phone = categories.stream().filter(c -> c.getName().equals("Điện thoại")).findFirst().map(Category::getName).orElse("Điện thoại");
                String laptop = categories.stream().filter(c -> c.getName().equals("Laptop")).findFirst().map(Category::getName).orElse("Laptop");
                String accessory = categories.stream().filter(c -> c.getName().equals("Phụ kiện")).findFirst().map(Category::getName).orElse("Phụ kiện");

                List<Product> products = Arrays.asList(
                    new Product("iPhone 15 Pro", "Apple", phone, new BigDecimal("29990000"),
                        "Màn hình 6.1 inch, chip A17 Pro, camera Pro.",
                        "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9", 20),
                    new Product("Samsung Galaxy S24", "Samsung", phone, new BigDecimal("24990000"),
                        "Hiệu năng mạnh, camera sắc nét.",
                        "https://images.unsplash.com/photo-1510557880182-3d4d3cba35a5", 25),
                    new Product("MacBook Air M3", "Apple", laptop, new BigDecimal("28990000"),
                        "Mỏng nhẹ, pin lâu, hiệu năng tốt.",
                        "https://images.unsplash.com/photo-1517336714731-489689fd1ca8", 15),
                    new Product("Dell XPS 13", "Dell", laptop, new BigDecimal("31990000"),
                        "Thiết kế cao cấp, màn hình đẹp.",
                        "https://images.unsplash.com/photo-1518770660439-4636190af475", 12),
                    new Product("Tai nghe Bluetooth", "Sony", accessory, new BigDecimal("1990000"),
                        "Âm thanh tốt, chống ồn.",
                        "https://images.unsplash.com/photo-1511367461989-f85a21fda167", 50),
                    new Product("Sạc nhanh 65W", "Anker", accessory, new BigDecimal("590000"),
                        "Sạc nhanh an toàn, nhỏ gọn.",
                        "https://images.unsplash.com/photo-1520174691701-bc555a3404ca", 100)
                );
                productRepository.saveAll(products);
            }

            if (userRepository.count() == 0) {
                User admin = new User("admin", passwordEncoder.encode("admin123"), "Quản trị", "ROLE_ADMIN");
                User user = new User("user", passwordEncoder.encode("user123"), "Khách hàng", "ROLE_USER");
                userRepository.saveAll(Arrays.asList(admin, user));
            }
        };
    }
}
