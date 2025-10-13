package com.makemytrip.makemytrip.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    private String id;
    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String email;

    private String password;
    private String role;
    private String phoneNumber;

    // 👇 Add this field for embedding all user bookings
    @Field("bookings")
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

}
