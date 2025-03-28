package com.example.notificationapp.api_service;

import com.example.notificationapp.api_service.controller.ContactController;
import com.example.notificationapp.api_service.dto.ContactCreateDTO;
import com.example.notificationapp.api_service.dto.ContactUpdateDTO;
import com.example.notificationapp.api_service.entity.Contact;
import com.example.notificationapp.api_service.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContactControllerTest {

    @InjectMocks
    private ContactController contactController;

    @Mock
    private ContactService contactService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Тест для метода uploadContacts, когда импорт проходит успешно
    @Test
    public void testUploadContactsSuccess() throws Exception {
        // Создаем тестовый файл (например, пустой, поскольку логика импорта проверяется в сервисе)
        MockMultipartFile file = new MockMultipartFile("file", "contacts.csv", "text/csv", "full_name,email,phone_number,messenger_type,messenger_id\nJohn Doe,john@example.com,123456789,telegram,123".getBytes());

        // Выполнение метода
        ResponseEntity<?> response = contactController.uploadContacts(file);

        // Проверяем, что сервис был вызван и возвращается статус 200 OK
        verify(contactService, times(1)).importContacts(file);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Contacts successfully imported", response.getBody());
    }

    // Тест для метода uploadContacts, когда происходит исключение
    @Test
    public void testUploadContactsFailure() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "contacts.csv", "text/csv", "invalid content".getBytes());

        // Настраиваем сервис, чтобы выбросить исключение
        doThrow(new Exception("Parsing error")).when(contactService).importContacts(file);

        ResponseEntity<?> response = contactController.uploadContacts(file);

        // Проверяем, что возвращается статус BAD_REQUEST с сообщением об ошибке
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Parsing error"));
    }

    // Тест для получения списка контактов
    @Test
    public void testGetContacts() {
        // Создаем фиктивную страницу контактов
        Contact contact = new Contact();
        contact.setFullName("John Doe");
        Page<Contact> page = new PageImpl<>(Collections.singletonList(contact));
        when(contactService.getContacts(PageRequest.of(0, 10))).thenReturn(page);

        ResponseEntity<Page<Contact>> response = contactController.getContacts(0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }

    // Тест для создания контакта при отсутствии ошибок валидации
    @Test
    public void testCreateContactSuccess() {
        ContactCreateDTO dto = new ContactCreateDTO();
        dto.setFullName("Jane Doe");
        dto.setEmail("jane@example.com");
        dto.setPhoneNumber("987654321");
        dto.setMessengerType("whatsapp");
        dto.setMessengerId("456");

        BindingResult bindingResult = new BeanPropertyBindingResult(dto, "dto");

        Contact createdContact = new Contact();
        createdContact.setFullName(dto.getFullName());
        createdContact.setEmail(dto.getEmail());
        when(contactService.createContact(dto)).thenReturn(createdContact);

        ResponseEntity<?> response = contactController.createContact(dto, bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof Contact);
        Contact returnedContact = (Contact) response.getBody();
        assertEquals("Jane Doe", returnedContact.getFullName());
    }

    // Тест для создания контакта с ошибками валидации
    @Test
    public void testCreateContactValidationErrors() {
        ContactCreateDTO dto = new ContactCreateDTO();
        // Не задаем обязательные поля, чтобы вызвать ошибки валидации
        BindingResult bindingResult = new BeanPropertyBindingResult(dto, "dto");
        bindingResult.rejectValue("fullName", "NotEmpty", "Full name is required");

        ResponseEntity<?> response = contactController.createContact(dto, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // Ожидаем список ошибок
        List<String> errors = (List<String>) response.getBody();
        assertFalse(errors.isEmpty());
        assertTrue(errors.get(0).contains("fullName"));
    }

    // Тест для получения контакта по id
    @Test
    public void testGetContactById() {
        Contact contact = new Contact();
        contact.setFullName("John Doe");
        when(contactService.getContactById(1L)).thenReturn(contact);

        ResponseEntity<Contact> response = contactController.getContactById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("John Doe", response.getBody().getFullName());
    }

    // Тест для обновления контакта без ошибок валидации
    @Test
    public void testUpdateContactSuccess() {
        ContactUpdateDTO dto = new ContactUpdateDTO();
        dto.setFullName("Updated Name");
        dto.setEmail("updated@example.com");
        dto.setPhoneNumber("000111222");
        dto.setMessengerType("telegram");
        dto.setMessengerId("789");

        BindingResult bindingResult = new BeanPropertyBindingResult(dto, "dto");

        Contact updatedContact = new Contact();
        updatedContact.setFullName(dto.getFullName());
        when(contactService.updateContact(1L, dto)).thenReturn(updatedContact);

        ResponseEntity<?> response = contactController.updateContact(1L, dto, bindingResult);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Contact);
        Contact result = (Contact) response.getBody();
        assertEquals("Updated Name", result.getFullName());
    }

    // Тест для обновления контакта с ошибками валидации
    @Test
    public void testUpdateContactValidationErrors() {
        ContactUpdateDTO dto = new ContactUpdateDTO();
        BindingResult bindingResult = new BeanPropertyBindingResult(dto, "dto");
        bindingResult.rejectValue("email", "NotEmpty", "Email is required");

        ResponseEntity<?> response = contactController.updateContact(1L, dto, bindingResult);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        List<String> errors = (List<String>) response.getBody();
        assertFalse(errors.isEmpty());
        assertTrue(errors.get(0).contains("email"));
    }

    // Тест для удаления контакта
    @Test
    public void testDeleteContact() {
        // Для delete нет возвращаемого объекта, просто проверяем вызов сервиса
        doNothing().when(contactService).deleteContact(1L);
        ResponseEntity<?> response = contactController.deleteContact(1L);
        verify(contactService, times(1)).deleteContact(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Contact deleted successfully", response.getBody());
    }
}
