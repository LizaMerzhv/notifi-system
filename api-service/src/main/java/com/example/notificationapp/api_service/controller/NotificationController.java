package com.example.notificationapp.api_service.controller;

import com.example.notificationapp.api_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
