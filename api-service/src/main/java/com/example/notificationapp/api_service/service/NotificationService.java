package com.example.notificationapp.api_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendNotification(String topic, String message) {
        System.out.println("Notification sent to topic notifications: " + topic + " | " + message);
        kafkaTemplate.send(topic, message);
    }
}
