package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {
    private final OrderRepository orderRepository;

    public AdminOrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public String list(@RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       Model model) {
        Page<Order> orderPage = orderRepository.findByFullNameContainingIgnoreCase(
            keyword == null ? "" : keyword, PageRequest.of(page, 10));
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("page", orderPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("statuses", getStatuses());
        return "admin/orders";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable String id, Model model) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return "redirect:/admin/orders";
        }
        model.addAttribute("order", order);
        return "admin/order-detail";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable String id, @RequestParam String status) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
        return "redirect:/admin/orders";
    }

    @PostMapping("/{id}/paid")
    public String updatePaid(@PathVariable String id, @RequestParam boolean paid) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setPaid(paid);
            order.setPaidAt(paid ? LocalDateTime.now() : null);
            orderRepository.save(order);
        }
        return "redirect:/admin/orders/" + id;
    }

    private List<String> getStatuses() {
        return Arrays.asList("CHO_THANH_TOAN", "CHO_XU_LY", "DANG_GIAO", "HOAN_TAT", "HUY");
    }
}
