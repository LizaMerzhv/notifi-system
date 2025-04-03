package com.example.notificationapp.api_service.controller;

import com.example.notificationapp.api_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<?> sendNotification(@RequestParam String topic, @RequestParam String message) {
        notificationService.sendNotification(topic, message);
        return ResponseEntity.ok("Notification sent to topic " + topic);
    }

    @PostMapping("/template")
    public ResponseEntity<?> sendTemplateNotification(
            @RequestParam String topic,
            @RequestParam Long templateId,
            @RequestBody Map<String, String> values) {
        notificationService.sendTemplateNotification(topic, templateId, values);
        return ResponseEntity.ok("Template notification sent to topic " + topic);
    }

}
