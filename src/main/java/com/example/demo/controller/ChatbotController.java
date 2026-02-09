package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.service.GeminiChatService;

@Controller
@SessionAttributes("chatHistory")
public class ChatbotController {
    private final GeminiChatService geminiChatService;

    public ChatbotController(GeminiChatService geminiChatService) {
        this.geminiChatService = geminiChatService;
    }

    @ModelAttribute("chatHistory")
    public List<String> chatHistory() {
        return new ArrayList<>();
    }

    @GetMapping("/chatbot")
    public String chat(Model model) {
        return "chatbot";
    }

    @PostMapping("/chatbot")
    public String ask(@RequestParam String message, @ModelAttribute("chatHistory") List<String> chatHistory) {
        chatHistory.add("Bạn: " + message);
        chatHistory.add("Bot: " + geminiChatService.generateReply(message, chatHistory));
        return "redirect:/chatbot";
    }
}
