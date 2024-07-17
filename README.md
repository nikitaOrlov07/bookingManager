# Booking Application

![Booking App Logo](path/to/your/logo.png)

## Table of Contents
- [Overview](#overview)
- [Features](#features)
  - [Unauthenticated Users](#unauthenticated-users)
  - [Authenticated Users](#authenticated-users)
  - [Admin Users](#admin-users)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Contributing](#contributing)
- [License](#license)

## Overview

This Booking Application is a comprehensive platform for managing and browsing various types of accommodations. It caters to different user roles, providing a range of functionalities from simple browsing to advanced booking management.

## Features

### Unauthenticated Users

- Browse all available bookings
- Search bookings by:
  - Booking type (hostel, hotel, etc.)
  - Occupancy status
  - Country
  - City
  - Address
  - Title
  - Company name
- Sort search results by capacity or price
- View detailed booking information:
  - Host verification status
  - Location details
  - Price and currency
  - Description
  - Capacity and room details
  - Rating
  - Amenities and conditions
  - Attachments (images and files)
- View host information and other company bookings
- Read reviews
- Register or log in

### Authenticated Users

All features of unauthenticated users, plus:

- Add and delete own reviews
- Access to configuration page:
  - View profile details
  - Delete profile
  - View submitted and accepted booking requests
- Create new bookings with a user-friendly interface
- Update and delete own bookings (or company bookings)
- Manage attachments for bookings
- Real-time chat with other users (secure and private)

### Admin Users

All features of authenticated users, plus:

- Delete or update information in all bookings
- Access to admin dashboard:
  - View all users
  - View all bookings

## Technologies Used

- Backend: Java (Spring Boot application)
- Frontend: JavaScript, CSS, HTML
- Real-time Chat: WebSocket and JavaScript
- 
## Screenshots

### Main Page
![image](https://github.com/user-attachments/assets/88634958-8c9b-4dcb-bb56-c850ba75bce6)
*main page in the application where all the necessary information is located.*

### Detail Page
![image](https://github.com/user-attachments/assets/4b04e70f-3036-4547-9b97-e1277f3d6635)
![image](https://github.com/user-attachments/assets/9c389678-a1b4-4622-97f8-3214d7db9d23)
*page , where you can see all information*

### Create Booking Page 
![image](https://github.com/user-attachments/assets/fb67cd0d-f6a4-402d-8fa7-75da87fd1a39)
*page, where you can set information , that you want, and create your booking* 

### Configuration Page (for regular user)
![image](https://github.com/user-attachments/assets/ba9e33bb-ea8c-4ac1-9e67-777bbb85dd52)
*configuration page*

### Configuration Page (for bookings creator)
![image](https://github.com/user-attachments/assets/47a44fee-eee6-48fd-b751-6230fd4a22e9)
*configuration page*

### Configuration Page (for bookings admin)
![image](https://github.com/user-attachments/assets/a1282392-e354-4d2a-927a-f3d824c5aa5d)
![image](https://github.com/user-attachments/assets/daff654b-185b-4f81-8137-028d7d3666b2)
*configuration page*

### Chat page
![image](https://github.com/user-attachments/assets/2a6bde33-2646-447b-b3e7-96feba514e2c)
*page where the user can communicate with the bookings creator*


