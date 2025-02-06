package com.exeg2.tripmate.service;

import com.exeg2.tripmate.dto.request.*;
import com.exeg2.tripmate.dto.response.AuthenticateResponse;
import com.exeg2.tripmate.dto.response.IntrospectResponse;
import com.exeg2.tripmate.dto.response.ResetPasswordResponse;
import com.exeg2.tripmate.dto.response.SendEmailResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticateResponse authenticate(AuthenticateRequest request);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    String logout(LogoutRequest request);
    AuthenticateResponse refresh(RefreshRequest request) throws ParseException, JOSEException;
    SendEmailResponse isSentResetLink(SendEmailRequest request);
    ResetPasswordResponse resetPassword(ResetPasswordRequest request, String token);
}
