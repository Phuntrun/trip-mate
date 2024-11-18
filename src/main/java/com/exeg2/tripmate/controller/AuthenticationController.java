package com.exeg2.tripmate.controller;

import com.exeg2.tripmate.dto.request.AuthenticateRequest;
import com.exeg2.tripmate.dto.request.IntrospectRequest;
import com.exeg2.tripmate.dto.request.LogoutRequest;
import com.exeg2.tripmate.dto.request.RefreshRequest;
import com.exeg2.tripmate.dto.response.ApiResponse;
import com.exeg2.tripmate.dto.response.AuthenticateResponse;
import com.exeg2.tripmate.dto.response.IntrospectResponse;
import com.exeg2.tripmate.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    public ApiResponse<AuthenticateResponse> authenticate(@RequestBody AuthenticateRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticateResponse>builder().result(result).build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticateResponse> refresh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refresh(request);
        return ApiResponse.<AuthenticateResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody LogoutRequest request) {
        var message = authenticationService.logout(request);
        return ApiResponse.<String>builder()
                .message(message == null ? "You have been logout!" : message)
                .build();
    }
}
