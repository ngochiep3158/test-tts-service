package com.doan.tts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/api/tts")
public class TtsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtsApplication.class, args);
    }

    @PostMapping("/convert")
    public ResponseEntity<Map<String, Object>> convertTextToSpeech(@RequestBody Map<String, String> payload) {
        String text = payload.get("text");
        String lang = payload.getOrDefault("lang", "vi");
        Map<String, Object> response = new HashMap<>();

        if (text == null || text.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Vui lòng cung cấp văn bản (text)");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // Reproduce the behavior of the google-tts-api library
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
            String audioUrl = "https://translate.google.com/translate_tts?ie=UTF-8&client=tw-ob&tl=" + lang + "&q=" + encodedText;
            
            response.put("success", true);
            response.put("originalText", text);
            response.put("language", lang);
            response.put("audioUrl", audioUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi tạo giọng đọc");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}