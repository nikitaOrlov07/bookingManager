package com.example.bookingproject.Model;

import com.example.bookingproject.Config.BookingTime;
import com.example.bookingproject.Config.BookingType;
import com.example.bookingproject.Config.Currency;
import com.example.bookingproject.Model.Security.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
    private  Boolean occupied ; // will be false if neihborCount = capacity
    private  String companyName;

    // Amenities
    @ElementCollection
    private List<String> amenities = new ArrayList<>();  // modifiable collections
    // Conditions
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

    // Amount
    private int capacity = 0;
    private int numberOfRooms;


    // Users
    @ManyToMany
    @JsonBackReference
    @JoinTable(
            name = "booking_confirmed_users",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> confirmedUsers = new HashSet<>();

    @ManyToMany
    @JsonBackReference
    @JoinTable(
            name = "booking_requesting_users",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserEntity> requestingUsers = new HashSet<>();

    public void removeRequestingUser(UserEntity user) {
        requestingUsers.remove(user);
        user.getRequestingBookings().remove(this);
    }

    public void addConfirmedUser(UserEntity user) {
        confirmedUsers.add(user);
        user.getConfirmedBookings().add(this);
    }

    public void removeConfirmedUser(UserEntity user) {
        confirmedUsers.remove(user);
        user.getConfirmedBookings().remove(this);

    }
    // Comments For BookingEntity
    @ToString.Exclude
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Comment> comments;

    // Attachments
    @OneToMany(mappedBy = "booking", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonManagedReference
    private List<Attachment> attachments = new ArrayList<>();

    // Advert author
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonBackReference
    private UserEntity author;

    // Rating
    private Double rating ;
    public Double calculateAverageRating() {
        if (comments == null || comments.isEmpty()) {
            return 1.0;
        }
        double sum = comments.stream()
                .mapToDouble(Comment::getRating)
                .sum();
        return Math.round((sum / comments.size()) * 10.0) / 10.0; // made round to one decimal place
    }

}
