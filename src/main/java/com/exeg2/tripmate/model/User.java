package com.exeg2.tripmate.model;

import com.exeg2.tripmate.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

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

    @ManyToMany
    Set<Role> roles;


}
