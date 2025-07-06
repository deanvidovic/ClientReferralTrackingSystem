CREATE TABLE USERS (
    id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER'
);

INSERT INTO USERS (username, password, email, first_name, last_name, phone_number, role) VALUES
    ('deaan', '$2a$12$aknIb6M0p7DewgVpk3RONuOR4hE.qw3XRtrOiZEKUo5LLMEJq2Kgy', 'vidovicdean@gmail.com', 'Dean', 'Vidovic', '0992959503', 'USER'), -- Dean123!
    ('marrin', '$2a$12$rX.D0YHisS9V6VZFiE4K3eEr5iH/39Nki7H4I4vv980xt8Nf6MMCy', 'vidovicmarin@gmail.com', 'Marin', 'Vidovic', '0991234567','USER'), -- Marin123!
    ('lovvro', '$2a$12$p8hPRIsDchAkHWHWbayMUOjfGI5tkPi9IbrCV6fkc7Ub7Sei3alCK', 'bilanoviclovro@gmail.com', 'Lovro', 'Bilanovic', '0997654321','USER'), -- Lovro123!
    ('marrtin', '$2a$12$bt4PBh./Fuw47fi1OsVrZOY73N7jGPpw6QplRniGnbskTtfftzI2u', 'dumicmartin@gmail.com', 'Martin', 'Dumic', '0911234567','USER'), -- Martin123!
    ('admin', '$2a$12$zBX8yF1dxjDaVoWwbxpTPedcr1vRsu5GmWq.2mxnMKqF9Y57kGO8u', 'admin@system.com', 'Admin', 'System', '0981234567','ADMIN'); -- Admin123!


CREATE TABLE REFERRED_CLIENT (
     id INT AUTO_INCREMENT PRIMARY KEY,
     first_name VARCHAR(255) NOT NULL,
     last_name VARCHAR(255) NOT NULL,
     email VARCHAR(255),
     phone_number VARCHAR(50),
     is_currently_recommended BOOLEAN DEFAULT FALSE,
     created_by INT NOT NULL
);

CREATE TABLE REFERRAL (
    id INT AUTO_INCREMENT PRIMARY KEY,
    referrer_user_id INT NOT NULL,
    referred_client_id INT NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (referrer_user_id) REFERENCES USERS(id),
    FOREIGN KEY (referred_client_id) REFERENCES REFERRED_CLIENT(id) ON DELETE CASCADE
);

CREATE TABLE REWARD (
    id INT AUTO_INCREMENT PRIMARY KEY,
    referral_id INT NOT NULL,
    description TEXT,
    issued_date DATE NOT NULL,
    amount DECIMAL(10, 2),
    FOREIGN KEY (referral_id) REFERENCES REFERRAL(id)
);