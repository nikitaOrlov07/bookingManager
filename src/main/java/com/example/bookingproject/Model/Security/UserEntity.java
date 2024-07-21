package com.example.bookingproject.Model.Security;


import com.example.bookingproject.Model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String town;
    private Long phoneNumber;
    private int roleId; // {0,1}
    private String creationDate;
    private String companyName;
    private Boolean verified = false ;


    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_role",joinColumns = {@JoinColumn(name ="user_id",referencedColumnName ="id")},
            inverseJoinColumns ={@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private List<RoleEntity> roles = new ArrayList<>();
    public boolean hasAdminRole() {
        if (roles == null) {
            return false;
        }
        for (RoleEntity role : roles) {
            if (role.getName().equals("ADMIN")) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL , orphanRemoval = true)  // one user --> many comments in comment side i have @ ManyToone annotation
    private List<Message> messages = new ArrayList<>();
    // user confirmed books
    @ManyToMany(mappedBy = "confirmedUsers")
    @JsonIgnore
    private Set<BookingEntity> confirmedBookings = new HashSet<>();

    @ManyToMany(mappedBy = "requestingUsers")
    @JsonIgnore
    private Set<BookingEntity> requestingBookings = new HashSet<>();

    public void addBookRequest(BookingEntity booking) {
        requestingBookings.add(booking);
        booking.getRequestingUsers().add(this);
    }

    public void removeBookRequest(BookingEntity booking) {
        requestingBookings.remove(booking);
        booking.getRequestingUsers().remove(this);
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "participants", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE , CascadeType.REFRESH})
    private List<Chat> chats = new ArrayList<>();


    @OneToMany(fetch = FetchType.EAGER ,mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private List<BookingEntity> authoredBookings = new ArrayList<>();
    // Comments
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_liked_comments",
            joinColumns = {@JoinColumn(name ="user_id",referencedColumnName ="id")},
            inverseJoinColumns ={@JoinColumn(name = "comment_id", referencedColumnName = "id")}
    )
    private List<Comment> likedComments = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_disliked_comments",
            joinColumns = {@JoinColumn(name ="user_id",referencedColumnName ="id")},
            inverseJoinColumns ={@JoinColumn(name = "comment_id", referencedColumnName = "id")}
    )
    private List<Comment> dislikedComments = new ArrayList<>();

    // without this -> will be error in ChatController - deleteChat
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(getId(), that.getId());
    }
    //  Grievance
    @OneToMany(mappedBy = "complainant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Grievance> filedGrievances = new ArrayList<>();

    @OneToMany(mappedBy = "reportedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Grievance> receivedGrievances = new ArrayList<>();
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
