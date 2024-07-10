package com.example.bookingproject.Model;

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
    private Long id;

    private String title;
    @Enumerated(EnumType.STRING)
    private BookingType type;

    @Column(columnDefinition = "TEXT")
    private String description;
    private  Boolean occupied;
    private Double rating;
    @ElementCollection
    List<String> amenities;

    // Money
    private Double price;
    @Enumerated(EnumType.STRING)
    private Currency currency;

    // Location
    private String country;
    private String city;
    private String address;

    // amount
    private int neighborCount;
    private int capacity;
    private int numberOfRooms;


    // Users
    @ManyToMany(mappedBy = "userBooks")
    private List<UserEntity> confirmedUsers = new ArrayList<>();

    @ManyToMany(mappedBy = "bookRequest")
    private List<UserEntity> requestingUsers = new ArrayList<>();

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
