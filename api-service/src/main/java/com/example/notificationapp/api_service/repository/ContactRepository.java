package com.example.notificationapp.api_service.repository;
import com.example.notificationapp.api_service.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {

}
