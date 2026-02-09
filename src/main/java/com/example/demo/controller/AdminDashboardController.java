package com.example.demo.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;

@Controller
public class AdminDashboardController {
    private final OrderRepository orderRepository;

    public AdminDashboardController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/admin")
    public String dashboard(Model model) {
        List<Order> orders = orderRepository.findAll();
        BigDecimal totalRevenue = orders.stream()
            .filter(o -> "HOAN_TAT".equals(o.getStatus()))
            .map(Order::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> dailyRevenue = buildDailyRevenue(orders);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("dailyRevenue", dailyRevenue);
        model.addAttribute("totalOrders", orders.size());
        return "admin/dashboard";
    }

    private Map<String, BigDecimal> buildDailyRevenue(List<Order> orders) {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate day = today.minusDays(i);
            result.put(day.toString(), BigDecimal.ZERO);
        }

        for (Order order : orders) {
            if (!"HOAN_TAT".equals(order.getStatus()) || order.getCreatedAt() == null) {
                continue;
            }
            LocalDate date = order.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate();
            if (result.containsKey(date.toString())) {
                result.put(date.toString(), result.get(date.toString()).add(order.getTotal()));
            }
        }
        return result;
    }
}
