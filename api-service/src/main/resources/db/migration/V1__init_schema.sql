CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE contacts (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255),
    email VARCHAR(255),
    phone_number VARCHAR(20),
    messenger_type VARCHAR(50),
    messenger_id VARCHAR(255)
);

CREATE TABLE contact_groups (
    id SERIAL PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL
);

CREATE TABLE contact_group_members (
    contact_id INT REFERENCES contacts(id),
    group_id INT REFERENCES contact_groups(id),
    PRIMARY KEY (contact_id, group_id)
);

CREATE TABLE notification_templates (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),
    body TEXT,
    placeholders TEXT
);

CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    template_id INT REFERENCES notification_templates(id),
    group_id INT REFERENCES contact_groups(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50)
);

CREATE TABLE delivery_statuses (
    id SERIAL PRIMARY KEY,
    notification_id INT REFERENCES notifications(id),
    contact_id INT REFERENCES contacts(id),
    status VARCHAR(50),
    attempts_count INT,
    last_attempt_at TIMESTAMP
);