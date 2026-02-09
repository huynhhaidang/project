package com.example.demo.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.CheckoutRequest;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("cart")
public class CheckoutController {
    private final CartService cartService;
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public CheckoutController(CartService cartService, OrderService orderService, UserRepository userRepository, OrderRepository orderRepository) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/checkout")
    public String checkoutForm(@ModelAttribute("cart") Map<String, CartItem> cart, Model model, Principal principal) {
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }
        CheckoutRequest request = new CheckoutRequest();
        if (principal != null) {
            User user = userRepository.findByUsername(principal.getName()).orElse(null);
            if (user != null) {
                request.setFullName(user.getFullName());
                request.setPhone(user.getPhone());
                request.setEmail(user.getEmail());
                request.setShippingAddress(user.getShippingAddress());
            }
        }
        model.addAttribute("checkoutRequest", request);
        model.addAttribute("items", cartService.getItems(cart));
        model.addAttribute("total", cartService.getTotal(cart));
        return "checkout";
    }

    @PostMapping("/checkout")
    public String checkout(@Valid @ModelAttribute CheckoutRequest checkoutRequest,
                           BindingResult bindingResult,
                           @ModelAttribute("cart") Map<String, CartItem> cart,
                           Principal principal,
                           Model model) {
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("items", cartService.getItems(cart));
            model.addAttribute("total", cartService.getTotal(cart));
            return "checkout";
        }

        try {
            User user = userRepository.findByUsername(principal.getName()).orElseThrow();
            if (checkoutRequest.isSaveAddress()) {
                user.setFullName(checkoutRequest.getFullName());
                user.setPhone(checkoutRequest.getPhone());
                user.setEmail(checkoutRequest.getEmail());
                user.setShippingAddress(checkoutRequest.getShippingAddress());
                userRepository.save(user);
            }
            Order order = orderService.createOrder(principal.getName(), checkoutRequest, cart);
            cartService.clear(cart);
            model.addAttribute("order", order);
            return "order-success";
        } catch (IllegalStateException ex) {
            model.addAttribute("items", cartService.getItems(cart));
            model.addAttribute("total", cartService.getTotal(cart));
            model.addAttribute("errorMessage", ex.getMessage());
            return "checkout";
        }
    }

    @GetMapping("/orders")
    public String myOrders(Principal principal, Model model) {
        String userId = userRepository.findByUsername(principal.getName()).orElseThrow().getId();
        model.addAttribute("orders", orderService.findOrdersByUser(userId));
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable String id, Principal principal, Model model) {
        String userId = userRepository.findByUsername(principal.getName()).orElseThrow().getId();
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null || !userId.equals(order.getUserId())) {
            return "redirect:/orders";
        }
        model.addAttribute("order", order);
        return "order-detail-user";
    }
}
