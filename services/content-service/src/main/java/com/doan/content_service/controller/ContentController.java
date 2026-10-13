package com.doan.content_service.controller;

import com.doan.content_service.dto.ContentRequest;
import com.doan.content_service.model.Content;
import com.doan.content_service.repository.ContentRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class ContentController {
    private final ContentRepository repository;

    public ContentController(ContentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("service", "content-service", "status", "ok");
    }

    @GetMapping("/contents")
    public List<Content> getAll() {
        return repository.findAll();
    }

    @GetMapping("/contents/{id}")
    public Content getById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nội dung"));
    }

    @PostMapping("/contents")
    @ResponseStatus(HttpStatus.CREATED)
    public Content create(@Valid @RequestBody ContentRequest request) {
        Content content = new Content();
        applyRequest(content, request);
        return repository.save(content);
    }

    @PutMapping("/contents/{id}")
    public Content update(@PathVariable Long id, @Valid @RequestBody ContentRequest request) {
        Content content = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nội dung"));
        applyRequest(content, request);
        return repository.save(content);
    }

    @DeleteMapping("/contents/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Content content = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nội dung"));
        repository.delete(content);
    }

    private void applyRequest(Content content, ContentRequest request) {
        content.setTitle(request.getTitle().trim());
        content.setBody(request.getBody().trim());
        content.setLocation(request.getLocation());
        content.setSourceLanguage(request.getSourceLanguage().trim().toLowerCase());
    }
}
