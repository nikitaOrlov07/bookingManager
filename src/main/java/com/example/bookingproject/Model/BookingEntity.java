package com.example.bookingproject.Model;

import com.example.bookingproject.Config.BookingTime;
import com.example.bookingproject.Config.BookingType;
import com.example.bookingproject.Config.Currency;
import com.example.bookingproject.Model.Security.UserEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name= "Bookings")
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    @Enumerated(EnumType.STRING)
    private BookingType type;

    @Column(columnDefinition = "TEXT")
    private String description;
    private  Boolean occupied; // will be false if neihborCount = capacity
    private Double rating;
    private  String companyName;

    // Conditions
    @ElementCollection
    private List<String> amenities = new ArrayList<>();  // modifiable collections

    @ElementCollection
    private List<String> conditions = new ArrayList<>(); // modifiable collections

    // Money
    private Double price;
    private BookingTime bookingTime;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    // Location
    private String country;
    private String city;
    private String address;

    // amount
    private int neighborCount; // will increase , when users book
    private int capacity;
    private int numberOfRooms;


    // Users
    @ManyToMany(mappedBy = "userBooks")
    private List<UserEntity> confirmedUsers = new ArrayList<>();

    @ManyToMany(mappedBy = "bookRequest")
    private List<UserEntity> requestingUsers = new ArrayList<>(); // те которые заплатили

    // Comments For BookingEntity
    @ToString.Exclude
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Comment> comments;

    // Attachments (fotos ,videos)
    @OneToMany(mappedBy = "booking", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonManagedReference
    private List<Attachment> attachments = new ArrayList<>();

    // Advert author
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;
}
