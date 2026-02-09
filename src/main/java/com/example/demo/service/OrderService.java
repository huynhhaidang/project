package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.CheckoutRequest;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.User;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(String username, CheckoutRequest request, Map<String, CartItem> cart) {
        User user = userRepository.findByUsername(username).orElseThrow();

        for (CartItem item : cart.values()) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product == null || !product.isActive() || product.getStock() < item.getQuantity()) {
                throw new IllegalStateException("Sản phẩm không đủ tồn kho hoặc đã ngừng bán");
            }
        }

        Order order = new Order();
        order.setUserId(user.getId());
        order.setFullName(request.getFullName());
        order.setPhone(request.getPhone());
        order.setEmail(request.getEmail());
        order.setShippingAddress(request.getShippingAddress());
        order.setItems(cart.values().stream()
            .map(item -> new OrderItem(item.getProductId(), item.getName(), item.getPrice(), item.getQuantity()))
            .collect(Collectors.toList()));
        order.setTotal(cart.values().stream()
            .map(CartItem::getTotal)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add));
        String method = request.getPaymentMethod();
        order.setPaymentMethod(method);
        order.setPaid(false);
        if ("PAYPAL".equalsIgnoreCase(method)) {
            order.setStatus("CHO_THANH_TOAN");
        } else {
            order.setStatus("CHO_XU_LY");
        }
        order.setCreatedAt(LocalDateTime.now());

        Order saved = orderRepository.save(order);

        for (CartItem item : cart.values()) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        return saved;
    }

    public List<Order> findOrdersByUser(String userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
