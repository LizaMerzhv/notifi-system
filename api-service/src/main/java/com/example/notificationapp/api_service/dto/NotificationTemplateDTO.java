package com.example.notificationapp.api_service.dto;

import jakarta.validation.constraints.NotEmpty;

public class NotificationTemplateDTO {

    @NotEmpty(message = "Title must not be empty")
    private String title;

    @NotEmpty(message = "Body must not be empty")
    private String body;

    private String placeholders;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(String placeholders) {
        this.placeholders = placeholders;
    }
}

