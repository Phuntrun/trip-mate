package com.exeg2.tripmate.controller;

import com.exeg2.tripmate.dto.request.*;
import com.exeg2.tripmate.dto.response.*;
import com.exeg2.tripmate.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name="Authentication Controller", description = "authenticate user")
public class AuthenticationController {
    AuthenticationService authenticationService;

    @Operation(summary = "Login", description = "authenticate and generate token")
    @PostMapping("/token")
    public ApiResponse<AuthenticateResponse> authenticate(@RequestBody AuthenticateRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticateResponse>builder().result(result).build();
    }

    @Operation(summary = "Introspect", description = "check availability of token")
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    @Operation(summary = "Refresh token", description = "refresh token's duration")
    @PostMapping("/refresh")
    public ApiResponse<AuthenticateResponse> refresh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refresh(request);
        return ApiResponse.<AuthenticateResponse>builder().result(result).build();
    }

    @Operation(summary = "Logout", description = "logout and disable token")
    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody LogoutRequest request) {
        var message = authenticationService.logout(request);
        return ApiResponse.<String>builder()
                .message(message == null ? "You have been logout!" : message)
                .build();
    }

    @Operation(summary = "Forgot password", description = "check email in system and send recovery email")
    @PostMapping("/forgot")
    public ApiResponse<SendEmailResponse> forgot(@RequestBody SendEmailRequest request) {
        return ApiResponse.<SendEmailResponse>builder()
                .result(authenticationService.isSentResetLink(request))
                .build();
    }

    @Operation(summary = "reset", description = "reset password")
    @PostMapping("/forgot/{token}")
    public ApiResponse<ResetPasswordResponse> resetPassword(@RequestBody @Valid ResetPasswordRequest request, @PathVariable("token") String token) {
        return ApiResponse.<ResetPasswordResponse>builder()
                .result(authenticationService.resetPassword(request, token))
                .message("Reset password completed")
                .build();
    }
}
