package com.example.bookingproject.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment { // for fotos and videos

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    private String creator;
    private String downloadUrl;
    private String viewUrl;
    private String timestamp;


     // that a class property must be mapped to a larger object in the database.
     @Lob
     @Column(columnDefinition = "oid")
     private byte[] data;

    public Attachment(String fileName, String fileType, byte[] data, String downloadUrl , String viewUrl) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.downloadUrl=downloadUrl;
        this.viewUrl= viewUrl;
    }
    @ManyToOne
    @JoinColumn(name = "booking_id")
    @JsonBackReference
    private BookingEntity booking;;

}
