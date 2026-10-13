package com.doan.translation_service.controller;

import com.doan.translation_service.dto.TranslationRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping
public class TranslationController {
    private final RestClient restClient = RestClient.create();

    @Value("${translation.api-url:https://libretranslate.com/translate}")
    private String translationApiUrl;

    @Value("${translation.api-key:}")
    private String translationApiKey;

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("service", "translation-service", "status", "ok");
    }

    @PostMapping("/translate")
    public Map<String, String> translate(@Valid @RequestBody TranslationRequest request) {
        String source = request.getSource().trim().toLowerCase();
        String target = request.getTarget().trim().toLowerCase();

        if (source.equals(target)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ngôn ngữ nguồn và ngôn ngữ đích phải khác nhau");
        }

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("q", request.getText());
        payload.put("source", source);
        payload.put("target", target);
        payload.put("format", "text");
        if (translationApiKey != null && !translationApiKey.isBlank()) {
            payload.put("api_key", translationApiKey);
        }

        try {
            Map<?, ?> providerResponse = restClient.post()
                    .uri(translationApiUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .body(Map.class);

            Object translated = providerResponse == null ? null : providerResponse.get("translatedText");
            if (!(translated instanceof String translatedText) || translatedText.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                        "Dịch vụ ngoài không trả về translatedText hợp lệ");
            }

            return Map.of(
                    "translated_text", translatedText,
                    "source", source,
                    "target", target,
                    "provider", "LibreTranslate"
            );
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Không gọi được dịch vụ dịch. Hãy kiểm tra URL, kết nối mạng và API key.");
        }
    }
}
