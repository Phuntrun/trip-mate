package com.exeg2.tripmate.service;

import com.exeg2.tripmate.dto.request.AuthenticateRequest;
import com.exeg2.tripmate.dto.request.IntrospectRequest;
import com.exeg2.tripmate.dto.request.LogoutRequest;
import com.exeg2.tripmate.dto.request.RefreshRequest;
import com.exeg2.tripmate.dto.response.AuthenticateResponse;
import com.exeg2.tripmate.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticateResponse authenticate(AuthenticateRequest request);
    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    String logout(LogoutRequest request);
    AuthenticateResponse refresh(RefreshRequest request) throws ParseException, JOSEException;
}
