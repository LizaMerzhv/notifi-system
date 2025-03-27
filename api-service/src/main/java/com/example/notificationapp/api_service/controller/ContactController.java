package com.example.notificationapp.api_service.controller;

import com.example.notificationapp.api_service.entity.Contact;
import com.example.notificationapp.api_service.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadContacts(@RequestParam("file") MultipartFile file) {
        try {
            contactService.importContacts(file);
            return ResponseEntity.ok("Контакты успешно импортированы");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка при импорте контактов: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<Contact>> getContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Contact> contacts = contactService.getContacts(PageRequest.of(page, size));
        return ResponseEntity.ok(contacts);
    }

}

