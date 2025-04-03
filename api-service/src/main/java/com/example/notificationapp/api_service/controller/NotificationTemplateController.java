package com.example.notificationapp.api_service.controller;

import com.example.notificationapp.api_service.dto.NotificationTemplateDTO;
import com.example.notificationapp.api_service.entity.NotificationTemplate;
import com.example.notificationapp.api_service.service.NotificationTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/templates")
public class NotificationTemplateController {

    @Autowired
    private NotificationTemplateService templateService;

    @PostMapping
    public ResponseEntity<?> createTemplate(@Valid @RequestBody NotificationTemplateDTO dto) {
        NotificationTemplate template = templateService.createTemplate(dto);
        return new ResponseEntity<>(template, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTemplate(@PathVariable Long id, @Valid @RequestBody NotificationTemplateDTO dto) {
        NotificationTemplate template = templateService.updateTemplate(id, dto);
        return ResponseEntity.ok(template);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTemplate(@PathVariable Long id) {
        NotificationTemplate template = templateService.getTemplateById(id);
        return ResponseEntity.ok(template);
    }

    @PostMapping("/{id}/preview")
    public ResponseEntity<?> previewTemplate(@PathVariable Long id, @RequestBody Map<String, String> values) {
        String rendered = templateService.renderTemplate(id, values);
        return ResponseEntity.ok(rendered);
    }
}
