package com.exeg2.tripmate.dto.response;

import com.exeg2.tripmate.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;

    String firstname;
    String lastname;
    String email;
    String phone;
    LocalDate dob;
    Gender gender;
    String address;

    String username;
    boolean enabled;
}
