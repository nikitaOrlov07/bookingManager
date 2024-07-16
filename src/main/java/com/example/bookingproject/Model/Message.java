package com.example.bookingproject.Model;


import com.example.bookingproject.WebSocketConf.MessageType;
import com.example.bookingproject.Model.Security.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
// JsonProperty -> field will participate in the process of serialisation (converting a Java object to JSON) and deserialisation (converting JSON to a Java object) using the Jackson library
@JsonIgnoreProperties(ignoreUnknown = true) // to specify to ignore any properties (fields) in JSON
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    @JsonProperty("text")
    private String text;

    @JsonProperty("author")
    private String author;

    @JsonProperty("pubDate")
    private String pubDate;

    @JsonProperty("type")
    private MessageType type;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserEntity user;
}