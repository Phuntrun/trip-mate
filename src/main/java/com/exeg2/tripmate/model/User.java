package com.exeg2.tripmate.model;

import com.exeg2.tripmate.enums.Gender;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String firstname;
    String lastname;
    String email;
    String phone;
    LocalDate dob;
    Gender gender;
    String address;

    String username;
    String password;
    boolean enabled;

}
