package com.example.notificationapp.api_service.service;

import com.example.notificationapp.api_service.dto.NotificationTemplateDTO;
import com.example.notificationapp.api_service.entity.NotificationTemplate;
import com.example.notificationapp.api_service.repository.NotificationTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationTemplateService {

    @Autowired
    private NotificationTemplateRepository templateRepository;

    @Cacheable(value = "templates", key = "#id")
    public NotificationTemplate getTemplateById(Long id) {
        return templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found with id " + id));
    }

    @CachePut(value = "templates", key = "#result.id")
    public NotificationTemplate createTemplate(NotificationTemplateDTO dto) {
        NotificationTemplate template = new NotificationTemplate();
        template.setTitle(dto.getTitle());
        template.setBody(dto.getBody());
        template.setPlaceholders(dto.getPlaceholders());
        return templateRepository.save(template);
    }

    @CachePut(value = "templates", key = "#id")
    public NotificationTemplate updateTemplate(Long id, NotificationTemplateDTO dto) {
        NotificationTemplate template = getTemplateById(id);
        template.setTitle(dto.getTitle());
        template.setBody(dto.getBody());
        template.setPlaceholders(dto.getPlaceholders());
        return templateRepository.save(template);
    }

    @CacheEvict(value = "templates", key = "#id")
    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }

    public String renderTemplate(Long templateId, Map<String, String> values) {
        NotificationTemplate template = getTemplateById(templateId);
        String renderedBody = template.getBody();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            renderedBody = renderedBody.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return renderedBody;
    }
}
