package com.example.demo.config;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;

@Component
public class DataSeeder implements CommandLineRunner {
    private static final int TARGET_PRODUCTS_PER_CATEGORY = 10;
    private static final List<String> IMAGE_POOL = List.of(
        "/theme/images/card-item1.jpg",
        "/theme/images/card-item2.jpg",
        "/theme/images/card-item3.jpg",
        "/theme/images/card-item4.jpg",
        "/theme/images/card-item5.jpg",
        "/theme/images/card-item6.jpg",
        "/theme/images/card-item7.jpg",
        "/theme/images/card-item8.jpg",
        "/theme/images/card-item9.jpg",
        "/theme/images/card-item10.jpg",
        "/theme/images/card-large-item1.jpg",
        "/theme/images/card-large-item2.jpg",
        "/theme/images/card-large-item3.jpg",
        "/theme/images/card-large-item4.jpg",
        "/theme/images/card-large-item5.jpg",
        "/theme/images/card-large-item6.jpg",
        "/theme/images/card-large-item7.jpg",
        "/theme/images/card-large-item8.jpg",
        "/theme/images/card-large-item9.jpg",
        "/theme/images/card-large-item10.jpg"
    );

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final Random random = new Random(42);

    public DataSeeder(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            return;
        }

        int imageIndex = 0;
        for (Category category : categories) {
            long existing = productRepository.countByCategoryName(category.getName());
            int toCreate = (int) Math.max(0, TARGET_PRODUCTS_PER_CATEGORY - existing);
            for (int i = 0; i < toCreate; i++) {
                int seq = (int) existing + i + 1;
                String name = category.getName() + " - Sản phẩm " + seq;
                String brand = pickBrand(category.getName(), seq);
                BigDecimal price = pickPrice(category.getName());
                String description = "Sản phẩm chính hãng, bảo hành 12 tháng. Phù hợp nhu cầu sử dụng hằng ngày.";
                String imageUrl = IMAGE_POOL.get(imageIndex % IMAGE_POOL.size());
                int stock = 5 + (seq % 20);

                Product product = new Product(name, brand, category.getName(), price, description, imageUrl, stock);
                product.setActive(true);
                productRepository.save(product);
                imageIndex++;
            }
        }
    }

    private String pickBrand(String categoryName, int seq) {
        String key = categoryName == null ? "" : categoryName.toLowerCase(Locale.ROOT);
        if (key.contains("điện thoại") || key.contains("phone")) {
            return pickFrom(new String[] { "Apple", "Samsung", "Xiaomi", "OPPO", "Vivo" }, seq);
        }
        if (key.contains("laptop") || key.contains("máy tính")) {
            return pickFrom(new String[] { "Apple", "Asus", "Dell", "HP", "Lenovo" }, seq);
        }
        if (key.contains("phụ kiện") || key.contains("access")) {
            return pickFrom(new String[] { "Anker", "Baseus", "Ugreen", "Belkin", "Aukey" }, seq);
        }
        return "TechShop";
    }

    private String pickFrom(String[] options, int seq) {
        return options[Math.abs(seq) % options.length];
    }

    private BigDecimal pickPrice(String categoryName) {
        String key = categoryName == null ? "" : categoryName.toLowerCase(Locale.ROOT);
        int min;
        int max;
        if (key.contains("điện thoại") || key.contains("phone")) {
            min = 10000000;
            max = 35000000;
        } else if (key.contains("laptop") || key.contains("máy tính")) {
            min = 15000000;
            max = 50000000;
        } else if (key.contains("phụ kiện") || key.contains("access")) {
            min = 200000;
            max = 3000000;
        } else {
            min = 1000000;
            max = 8000000;
        }
        int value = min + random.nextInt(Math.max(1, max - min));
        return BigDecimal.valueOf(value);
    }
}
