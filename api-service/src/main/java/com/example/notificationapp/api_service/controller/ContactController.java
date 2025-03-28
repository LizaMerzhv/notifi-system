package com.example.notificationapp.api_service.controller;

import com.example.notificationapp.api_service.dto.ContactCreateDTO;
import com.example.notificationapp.api_service.dto.ContactUpdateDTO;
import com.example.notificationapp.api_service.entity.Contact;
import com.example.notificationapp.api_service.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadContacts(@RequestParam("file") MultipartFile file) {
        try {
            contactService.importContacts(file);
            return ResponseEntity.ok("Contacts successfully imported");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error importing contacts: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<Contact>> getContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Contact> contacts = contactService.getContacts(PageRequest.of(page, size));
        return ResponseEntity.ok(contacts);
    }

    @PostMapping
    public ResponseEntity<?> createContact(@Valid @RequestBody ContactCreateDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        Contact createdContact = contactService.createContact(dto);
        return new ResponseEntity<>(createdContact, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        Contact contact = contactService.getContactById(id);
        return ResponseEntity.ok(contact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContact(@PathVariable Long id,
                                           @Valid @RequestBody ContactUpdateDTO dto,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        Contact updatedContact = contactService.updateContact(id, dto);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.ok("Contact deleted successfully");
    }
}
