# Booking Application

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Screenshots](#screenshots)
- [Getting Started](#getting-started)
- [Contributing](#contributing)
- [License](#license)

## Overview

The Booking Application is a comprehensive platform for managing and browsing various types of accommodations. It caters to different user roles, providing a range of functionalities from simple browsing to advanced booking management and administrative controls.

## Features

### 🌐 Unauthenticated Users
- Browse all available bookings
- Advanced search functionality
- Detailed booking information view
- Host information access

### 👤 Authenticated Users
All features of unauthenticated users, plus:
- Review management
- Profile configuration
- Booking creation and management
- Real-time chat
- Grievance submission

### 🛡️ Admin Users
All features of authenticated users, plus:
- Full booking control
- User management via admin dashboard
- Grievance management
- Delete user , Delete booking enity 

## Technologies Used

- Backend: Java (Spring Boot)
- Frontend: JavaScript, CSS, HTML
- Real-time Chat: WebSocket
- Devops: implement docker and kubernates
- Create an initial settings file for the database (for Docker containers and Kubernetes pods) -> create an Admin user -> USERNAME: ADMIN, PASSWORD: Admin
## Screenshots

### Main Page
![Main Page](https://github.com/user-attachments/assets/88634958-8c9b-4dcb-bb56-c850ba75bce6)
*Main page showcasing key information and navigation.*

### Detail Page
![Detail Page 1](https://github.com/user-attachments/assets/4b04e70f-3036-4547-9b97-e1277f3d6635)
![Detail Page 2](https://github.com/user-attachments/assets/9c389678-a1b4-4622-97f8-3214d7db9d23)
*Comprehensive booking details and information.*

### Create Booking Page 
![Create Booking](https://github.com/user-attachments/assets/fb67cd0d-f6a4-402d-8fa7-75da87fd1a39)
*User-friendly interface for creating new bookings.*

### Configuration Pages
#### Regular User
![Regular User Config](https://github.com/user-attachments/assets/ba9e33bb-ea8c-4ac1-9e67-777bbb85dd52)

#### Booking Creator
![Booking Creator Config](https://github.com/user-attachments/assets/47a44fee-eee6-48fd-b751-6230fd4a22e9)

#### Admin
![Admin Config 1](https://github.com/user-attachments/assets/f4b9f868-bcb8-429a-80f7-cbbd65c91a7b)
![Admin Config 2](https://github.com/user-attachments/assets/213d7128-a810-419c-9261-ffe6614c2ed9)
*Tailored configuration pages for different user roles.*

### Chat Page
![Chat Page](https://github.com/user-attachments/assets/2a6bde33-2646-447b-b3e7-96feba514e2c)
*Real-time communication interface between users and booking creators.*

