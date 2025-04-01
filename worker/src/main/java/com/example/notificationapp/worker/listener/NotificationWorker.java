package com.example.notificationapp.worker.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationWorker {

    @KafkaListener(topics = "notifications", groupId = "notificationGroup")
    public void listen(String message) {
        System.out.println("Received notification message: " + message);
    }
}

