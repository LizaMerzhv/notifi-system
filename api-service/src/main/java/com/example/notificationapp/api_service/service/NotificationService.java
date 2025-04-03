package com.example.notificationapp.api_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private NotificationTemplateService templateService;

    public void sendNotification(String topic, String message) {
        System.out.println("Notification sent to topic notifications: " + topic + " | " + message);
        kafkaTemplate.send(topic, message);
    }

    public void sendTemplateNotification(String topic, Long templateId, Map<String, String> values) {
        String renderedMessage = templateService.renderTemplate(templateId, values);
        System.out.println("Template notification sent to topic '" + topic + "': " + renderedMessage);
        kafkaTemplate.send(topic, renderedMessage);
    }
}
