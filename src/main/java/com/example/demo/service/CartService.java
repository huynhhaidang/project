package com.example.demo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.model.CartItem;
import com.example.demo.model.Product;

@Service
public class CartService {

    public Map<String, CartItem> createCart() {
        return new LinkedHashMap<>();
    }

    public void addToCart(Map<String, CartItem> cart, Product product, int quantity) {
        CartItem item = cart.get(product.getId());
        if (item == null) {
            item = new CartItem(product.getId(), product.getName(), product.getPrice(), 0, product.getImageUrl());
            cart.put(product.getId(), item);
        }
        item.setQuantity(item.getQuantity() + quantity);
    }

    public void updateQuantity(Map<String, CartItem> cart, String productId, int quantity) {
        CartItem item = cart.get(productId);
        if (item == null) {
            return;
        }
        if (quantity <= 0) {
            cart.remove(productId);
        } else {
            item.setQuantity(quantity);
        }
    }

    public void remove(Map<String, CartItem> cart, String productId) {
        cart.remove(productId);
    }

    public void clear(Map<String, CartItem> cart) {
        cart.clear();
    }

    public List<CartItem> getItems(Map<String, CartItem> cart) {
        return new ArrayList<>(cart.values());
    }

    public BigDecimal getTotal(Map<String, CartItem> cart) {
        return cart.values().stream()
            .map(CartItem::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
