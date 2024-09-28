package com.exeg2.tripmate.dto.request;

import com.exeg2.tripmate.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String firstname;
    String lastname;
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "EMAIL_VALID")
    String email;
    String phone;
    LocalDate dob;
    Gender gender;
    String address;
    @Size(min = 10, max = 30, message = "PASSWORD_VALID")
    String password;

    Set<String> roles;
}
