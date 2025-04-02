# Notification Delivery System

A scalable and fault-tolerant platform for sending mass notifications through various channels (email, SMS, messengers, push). Users can manage contacts, create templates, and send messages with guaranteed delivery — even in case of service or provider failures.

## Features

- Import and manage user contacts (CSV/XLS upload, manual entry)
- Contact groups and template management
- Multi-channel notification delivery (email, SMS, push, messengers)
- Kafka-based messaging and worker queue
- Delivery status tracking and auto-retry with rebalancer service
- Horizontally scalable architecture (up to 1M recipients)
- Role-based authentication and authorization
- Monitoring and logging (Prometheus, Grafana, ELK)

## Tech Stack

- **Java 17**, Spring Boot (Web, Security, Validation)
- **Apache Kafka** – messaging broker
- **PostgreSQL**, **Redis** – storage and caching
- **Docker**, **Docker Compose**
- **JUnit**, **Mockito**, **Testcontainers** – testing
- **Maven / Gradle**
- **Prometheus**, **Grafana**, **ELK** – observability
- Third-party integrations: **Amazon SES**, **Twilio**, **ApplePush**

## System Architecture

### Components

- **API Service**  
  Handles CRUD operations for contacts, groups, templates. Publishes notifications to Kafka.

- **Kafka**  
  Acts as a message queue. Workers consume notification tasks from relevant topics.

- **Worker**  
  Consumes messages from Kafka, sends messages via email/SMS/etc., updates delivery status in DB.

- **Rebalancer**  
  Periodically scans DB for failed messages and republishes them to Kafka for retry.

- **PostgreSQL**  
  Stores users, contacts, templates, delivery logs.

- **Redis**  
  Caches frequently used **notification templates** to speed up rendering and reduce DB load.

- **Third-party providers**  
  Email (Amazon SES), SMS (Twilio), Push (ApplePush), etc.

---

## 🛠️ TODO

The project is still under development. Below is the list of pending implementation steps:


1. **Mass notification delivery**  
   - Retrieve contacts by group ID  
   - Determine correct delivery channel  
   - Send messages (initially via mock services)  
   - Store delivery statuses  
   - Integrate with Amazon SES (email), Twilio (SMS)  

2. **Rebalancer implementation**  
   - Scan DB for undelivered messages  
   - Re-publish to retry topic in Kafka  
   - Update Worker to process retries and track attempts  

3. **Status tracking and delivery logs API**  
   - Implement endpoints to view notification history  
   - Add filters (by contact, status, date, etc.)   
