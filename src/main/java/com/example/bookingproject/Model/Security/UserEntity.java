package com.example.bookingproject.Model.Security;


import com.example.bookingproject.Model.BookingEntity;
import com.example.bookingproject.Model.Chat;
import com.example.bookingproject.Model.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY )
    private Long id;
    private String username;
    private String email;
    private String password;
    private String town;
    private Long phoneNumber;
    private int roleId; // {0,1}
    String creationDate;

    @ToString.Exclude
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
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_books",
            joinColumns = {@JoinColumn(name ="user_id",referencedColumnName ="id")},
            inverseJoinColumns ={@JoinColumn(name = "friend_user_id", referencedColumnName = "id")}
    )
    private List<BookingEntity> userBooks = new ArrayList<>();
    // user book request
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "book_request",
            joinColumns = {@JoinColumn(name ="user_id",referencedColumnName ="id")},
            inverseJoinColumns ={@JoinColumn(name = "book_id", referencedColumnName = "id")}
    )
    private List<BookingEntity> bookRequest  = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "participants", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE , CascadeType.REFRESH})
    private List<Chat> chats = new ArrayList<>();


    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<BookingEntity> authoredBookings = new ArrayList<>();


    // without this -> will be error in ChatController - deleteChat
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
