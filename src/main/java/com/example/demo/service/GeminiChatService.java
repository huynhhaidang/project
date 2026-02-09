package com.example.demo.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiChatService {
    private static final int MAX_HISTORY_LINES = 6;

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.model:gemini-1.5-flash}")
    private String model;

    @Value("${gemini.endpoint:https://generativelanguage.googleapis.com/v1beta}")
    private String endpoint;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateReply(String message, List<String> chatHistory) {
        if (apiKey == null || apiKey.isBlank()) {
            return fallbackReply(message);
        }

        try {
            String prompt = buildPrompt(message, chatHistory);
            String url = String.format("%s/models/%s:generateContent?key=%s", endpoint, model, apiKey.trim());

            Map<String, Object> body = new LinkedHashMap<>();
            List<Map<String, Object>> contents = new ArrayList<>();
            contents.add(Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", prompt))
            ));
            body.put("contents", contents);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, body, Map.class);
            String text = extractText(response);
            if (text != null && !text.isBlank()) {
                return text.trim();
            }
        } catch (Exception ex) {
            // Ignore and fall back to local reply
        }

        return fallbackReply(message);
    }

    private String buildPrompt(String message, List<String> chatHistory) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bạn là trợ lý bán hàng TechShop, trả lời ngắn gọn, lịch sự, tiếng Việt. ");
        sb.append("Hỗ trợ đổi trả, bảo hành, tư vấn mua điện thoại/laptop/phụ kiện. ");
        sb.append("Nếu thiếu thông tin, hãy hỏi thêm ngắn gọn.\n\n");

        if (chatHistory != null && !chatHistory.isEmpty()) {
            int start = Math.max(0, chatHistory.size() - MAX_HISTORY_LINES);
            for (int i = start; i < chatHistory.size(); i++) {
                sb.append(chatHistory.get(i)).append("\n");
            }
            sb.append("\n");
        }

        sb.append("Bạn: ").append(message).append("\n");
        sb.append("Bot:");
        return sb.toString();
    }

    private String extractText(Map<String, Object> response) {
        if (response == null) {
            return null;
        }

        Object candidatesObj = response.get("candidates");
        if (!(candidatesObj instanceof List<?> candidates) || candidates.isEmpty()) {
            return null;
        }

        Object firstCandidate = candidates.get(0);
        if (!(firstCandidate instanceof Map<?, ?> firstMap)) {
            return null;
        }

        Object contentObj = firstMap.get("content");
        if (!(contentObj instanceof Map<?, ?> contentMap)) {
            return null;
        }

        Object partsObj = contentMap.get("parts");
        if (!(partsObj instanceof List<?> parts) || parts.isEmpty()) {
            return null;
        }

        Object firstPart = parts.get(0);
        if (!(firstPart instanceof Map<?, ?> partMap)) {
            return null;
        }

        Object textObj = partMap.get("text");
        return textObj instanceof String ? (String) textObj : null;
    }

    private String fallbackReply(String message) {
        String msg = message == null ? "" : message.toLowerCase(Locale.ROOT);
        if (msg.contains("đổi trả") || msg.contains("hoàn tiền")) {
            return "Bạn có thể đổi trả trong 7 ngày nếu sản phẩm còn nguyên tem. Vui lòng liên hệ hotline 1900-xxx.";
        }
        if (msg.contains("tư vấn") || msg.contains("mua") || msg.contains("chọn")) {
            return "Bạn cần dùng cho học tập, làm việc hay gaming? Mình sẽ gợi ý cấu hình phù hợp.";
        }
        if (msg.contains("bảo hành")) {
            return "Sản phẩm được bảo hành 12 tháng. Bạn giữ hóa đơn để được hỗ trợ nhanh hơn.";
        }
        return "Mình có thể hỗ trợ đổi trả, tư vấn mua hàng, bảo hành. Bạn cần hỗ trợ gì?";
    }
}
