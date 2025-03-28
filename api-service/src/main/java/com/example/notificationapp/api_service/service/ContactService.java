package com.example.notificationapp.api_service.service;

import com.example.notificationapp.api_service.dto.ContactCreateDTO;
import com.example.notificationapp.api_service.dto.ContactUpdateDTO;
import com.example.notificationapp.api_service.entity.Contact;
import com.example.notificationapp.api_service.repository.ContactRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public void importContacts(MultipartFile file) throws Exception {
        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
            CSVFormat format = CSVFormat.DEFAULT
                    .withDelimiter(';')
                    .withHeader("full_name", "email", "phone_number", "messenger_type", "messenger_id")
                    .withFirstRecordAsHeader()
                    .withTrim();

            CSVParser parser = new CSVParser(reader, format);
            System.out.println("CSV Headers: " + parser.getHeaderMap());

            List<Contact> contacts = new ArrayList<>();
            for (CSVRecord record : parser) {
                String fullName = record.get("full_name");
                String email = record.get("email");
                Contact contact = new Contact();
                contact.setFullName(fullName);
                contact.setEmail(email);
                contact.setPhoneNumber(record.get("phone_number"));
                contact.setMessengerType(record.get("messenger_type"));
                contact.setMessengerId(record.get("messenger_id"));
                contacts.add(contact);
            }
            contactRepository.saveAll(contacts);
        }
    }

    public Page<Contact> getContacts(Pageable pageable) {
        return contactRepository.findAll(pageable);
    }

    public Contact createContact(ContactCreateDTO dto) {
        Contact contact = mapToEntity(dto);
        return contactRepository.save(contact);
    }

    public Contact getContactById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id " + id));
    }

    public Contact updateContact(Long id, ContactUpdateDTO dto) {
        Contact existingContact = getContactById(id);
        existingContact = updateEntity(existingContact, dto);
        return contactRepository.save(existingContact);
    }

    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }

    private Contact mapToEntity(ContactCreateDTO dto) {
        Contact contact = new Contact();
        contact.setFullName(dto.getFullName());
        contact.setEmail(dto.getEmail());
        contact.setPhoneNumber(dto.getPhoneNumber());
        contact.setMessengerType(dto.getMessengerType());
        contact.setMessengerId(dto.getMessengerId());
        return contact;
    }

    private Contact updateEntity(Contact contact, ContactUpdateDTO dto) {
        contact.setFullName(dto.getFullName());
        contact.setEmail(dto.getEmail());
        contact.setPhoneNumber(dto.getPhoneNumber());
        contact.setMessengerType(dto.getMessengerType());
        contact.setMessengerId(dto.getMessengerId());
        return contact;
    }
}
