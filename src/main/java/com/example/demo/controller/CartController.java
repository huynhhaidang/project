package com.example.demo.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.service.CartService;
import com.example.demo.service.ProductService;

@Controller
@SessionAttributes("cart")
public class CartController {
    private final CartService cartService;
    private final ProductService productService;

    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @ModelAttribute("cart")
    public Map<String, CartItem> cart() {
        return cartService.createCart();
    }

    @GetMapping("/cart")
    public String viewCart(@ModelAttribute("cart") Map<String, CartItem> cart, Model model) {
        model.addAttribute("items", cartService.getItems(cart));
        model.addAttribute("total", cartService.getTotal(cart));
        return "cart";
    }

    @PostMapping("/cart/add/{id}")
    public String addToCart(@PathVariable String id,
                            @RequestParam(defaultValue = "1") int quantity,
                            @ModelAttribute("cart") Map<String, CartItem> cart) {
        Product product = productService.findById(id).orElse(null);
        if (product != null && product.isActive() && product.getStock() > 0) {
            CartItem existing = cart.get(product.getId());
            int currentQty = existing != null ? existing.getQuantity() : 0;
            int available = product.getStock() - currentQty;
            if (available > 0) {
                int qtyToAdd = Math.min(quantity, available);
                cartService.addToCart(cart, product, qtyToAdd);
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/update/{id}")
    public String updateQuantity(@PathVariable String id,
                                 @RequestParam int quantity,
                                 @ModelAttribute("cart") Map<String, CartItem> cart) {
        Product product = productService.findById(id).orElse(null);
        if (product != null) {
            int safeQty = Math.min(quantity, product.getStock());
            cartService.updateQuantity(cart, id, safeQty);
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{id}")
    public String remove(@PathVariable String id,
                         @ModelAttribute("cart") Map<String, CartItem> cart) {
        cartService.remove(cart, id);
        return "redirect:/cart";
    }
}
