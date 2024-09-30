package com.exeg2.tripmate.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level =AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    USERNAME_USED(HttpStatus.BAD_REQUEST.value(), "Username was be use!", HttpStatus.BAD_REQUEST),
    EMAIL_USED(HttpStatus.BAD_REQUEST.value(), "Email was be use!", HttpStatus.BAD_REQUEST),
    PHONE_USED(HttpStatus.BAD_REQUEST.value(), "Phone was be use!", HttpStatus.BAD_REQUEST),
    USERNAME_VALID(HttpStatus.BAD_REQUEST.value(), "Username must be at least {min} characters and at most {max} characters!", HttpStatus.BAD_REQUEST),
    PASSWORD_VALID(HttpStatus.BAD_REQUEST.value(), "Password must be at least {min} characters and at most {max} characters!", HttpStatus.BAD_REQUEST),
    EMAIL_VALID(HttpStatus.BAD_REQUEST.value(), "Email must be in form example@gmail.com!", HttpStatus.BAD_REQUEST),
    INVALID_KEY(HttpStatus.BAD_REQUEST.value(), "Invalid enums key!", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "User not found!", HttpStatus.NOT_FOUND),
    PERMISSION_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Permission not found!", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Role not found!", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED.value(), "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "Unauthorized!", HttpStatus.UNAUTHORIZED),
    ;
    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
