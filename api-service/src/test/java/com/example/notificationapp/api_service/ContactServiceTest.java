package com.example.notificationapp.api_service;

import com.example.notificationapp.api_service.dto.ContactCreateDTO;
import com.example.notificationapp.api_service.dto.ContactUpdateDTO;
import com.example.notificationapp.api_service.entity.Contact;
import com.example.notificationapp.api_service.repository.ContactRepository;
import com.example.notificationapp.api_service.service.ContactService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContactServiceTest {

    @InjectMocks
    private ContactService contactService;

    @Mock
    private ContactRepository contactRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Тест для импорта контактов из CSV файла
    @Test
    public void testImportContacts() throws Exception {
        // Подготовка CSV-данных с разделителем ";"
        String csvContent = "full_name;email;phone_number;messenger_type;messenger_id\n" +
                "John Doe;john@example.com;123456789;telegram;123\n" +
                "Jane Smith;jane@example.com;987654321;whatsapp;456";
        MockMultipartFile file = new MockMultipartFile("file", "contacts.csv", "text/csv", csvContent.getBytes(StandardCharsets.UTF_8));

        // Симулируем успешное сохранение (метод saveAll возвращает переданный список)
        when(contactRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        contactService.importContacts(file);

        // Проверяем, что saveAll был вызван и количество импортированных контактов равно 2
        ArgumentCaptor<java.util.List<Contact>> captor = ArgumentCaptor.forClass(java.util.List.class);
        verify(contactRepository, times(1)).saveAll(captor.capture());
        assertEquals(2, captor.getValue().size());
    }

    // Тест для получения контактов с использованием пагинации
    @Test
    public void testGetContacts() {
        Contact contact = new Contact();
        contact.setFullName("John Doe");
        Page<Contact> page = new PageImpl<>(Collections.singletonList(contact));
        when(contactRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<Contact> result = contactService.getContacts(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("John Doe", result.getContent().get(0).getFullName());
    }

    // Тест для создания контакта
    @Test
    public void testCreateContact() {
        ContactCreateDTO dto = new ContactCreateDTO();
        dto.setFullName("Alice");
        dto.setEmail("alice@example.com");
        dto.setPhoneNumber("111222333");
        dto.setMessengerType("viber");
        dto.setMessengerId("789");

        Contact savedContact = new Contact();
        savedContact.setFullName(dto.getFullName());
        savedContact.setEmail(dto.getEmail());

        when(contactRepository.save(any(Contact.class))).thenReturn(savedContact);

        Contact result = contactService.createContact(dto);

        assertNotNull(result);
        assertEquals("Alice", result.getFullName());
        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    // Тест для получения контакта по id
    @Test
    public void testGetContactById_Found() {
        Contact contact = new Contact();
        contact.setFullName("Bob");
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        Contact result = contactService.getContactById(1L);
        assertNotNull(result);
        assertEquals("Bob", result.getFullName());
    }

    @Test
    public void testGetContactById_NotFound() {
        when(contactRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> {
            contactService.getContactById(1L);
        });
        assertTrue(exception.getMessage().contains("Contact not found with id"));
    }

    // Тест для обновления контакта
    @Test
    public void testUpdateContact() {
        Contact existingContact = new Contact();
        existingContact.setFullName("Old Name");
        when(contactRepository.findById(1L)).thenReturn(Optional.of(existingContact));
        when(contactRepository.save(any(Contact.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ContactUpdateDTO dto = new ContactUpdateDTO();
        dto.setFullName("New Name");
        dto.setEmail("new@example.com");
        dto.setPhoneNumber("999888777");
        dto.setMessengerType("skype");
        dto.setMessengerId("321");

        Contact result = contactService.updateContact(1L, dto);

        assertNotNull(result);
        assertEquals("New Name", result.getFullName());
        verify(contactRepository, times(1)).save(existingContact);
    }

    // Тест для удаления контакта
    @Test
    public void testDeleteContact() {
        doNothing().when(contactRepository).deleteById(1L);
        contactService.deleteContact(1L);
        verify(contactRepository, times(1)).deleteById(1L);
    }
}
