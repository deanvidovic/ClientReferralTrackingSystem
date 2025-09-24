# Client Referral Tracking System

A desktop application for tracking client referrals — a simple and efficient system for businesses that grow through word-of-mouth.  
Administrators can track referrals, manage rewards, and analyze performance in one place.

---

## Table of Contents

- [Features](#features)  
- [Tech Stack](#tech-stack)  
- [Installation & Setup](#installation--setup)  
- [Usage](#usage)  
- [Screenshots](#screenshots)  

---

## Features

- Register new clients and referrals (who referred whom)  
- Track and manage referral-based rewards  
- Admin login with role-based access  
- Manage users and their statuses  

---

## Tech Stack

| Component        | Technology / Tool |
|------------------|-------------------|
| Language         | Java 23 |
| UI Framework     | JavaFX |
| Database         | H2 (embedded) |
| Database Access  | JDBC |
| Build Tool       | Maven |
| Config           | `database.properties` + SQL scripts |

---

## Installation & Setup

1. Clone the repository:  
   ```bash
   git clone https://github.com/deanvidovic/ClientReferralTrackingSystem.git
   cd ClientReferralTrackingSystem

2. Place the configuration files directly in the project root folder.

3. Create the following file:
   - `database.properties`
   
      ```bash
      databaseUrl = jdbc:h2:tcp://localhost/~/yourDatabaseName
      username = yourUsername
      password = yourPassword
4. Initialize the database by running the SQL script:  `database.sql`

---

## Usage

- Admin logs in and manages users, referrals, and rewards
- New clients and referrals can be registered (track who referred whom)
- System tracks referral status (qualified / reward granted)
- User-friendly JavaFX interface

---

## Screenshots
### Login screen

<img width="1946" height="1089" alt="image" src="https://github.com/user-attachments/assets/4d879868-af45-4450-bf63-4cb327016cd6" />

---

### User dashboard

<img width="1947" height="1091" alt="image" src="https://github.com/user-attachments/assets/dcd9ee69-02d2-44ae-b1d6-2f108398cdce" />

---

### Admin dashboard

<img width="1942" height="1086" alt="image" src="https://github.com/user-attachments/assets/0087c9b8-0421-4146-830a-f60dc4f0e3fd" />



