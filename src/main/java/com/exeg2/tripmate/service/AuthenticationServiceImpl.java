package com.exeg2.tripmate.service;

import com.exeg2.tripmate.dto.request.*;
import com.exeg2.tripmate.dto.response.AuthenticateResponse;
import com.exeg2.tripmate.dto.response.IntrospectResponse;
import com.exeg2.tripmate.dto.response.ResetPasswordResponse;
import com.exeg2.tripmate.dto.response.SendEmailResponse;
import com.exeg2.tripmate.enums.ErrorCode;
import com.exeg2.tripmate.exception.AppException;
import com.exeg2.tripmate.model.InvalidToken;
import com.exeg2.tripmate.model.ResetPasswordToken;
import com.exeg2.tripmate.model.User;
import com.exeg2.tripmate.repository.InvalidTokenRepository;
import com.exeg2.tripmate.repository.ResetPasswordTokenRepository;
import com.exeg2.tripmate.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    InvalidTokenRepository invalidTokenRepository;
    ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final JavaMailSenderImpl mailSender;

    @NonFinal
    @Value("${spring.jwt.signkey}")
    protected String signKey;

    @NonFinal
    @Value("${spring.mail.username}")
    protected String hostMail;

    @NonFinal
    @Value("${spring.jwt.valid-duration}")
    protected long validationDuration;

    @NonFinal
    @Value("${spring.jwt.refreshable-duration}")
    protected long refreshableDuration;

    public AuthenticateResponse authenticate(AuthenticateRequest request) {
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        boolean authenticated = bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(user);

        return AuthenticateResponse.builder().authenticated(true).token(token).build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        boolean valid = true;
        try {
            verifyToken(request.getToken(), false);
        } catch (AppException e) {
            valid = false;
        }

        return IntrospectResponse.builder().valid(valid).build();
    }

    public String logout(LogoutRequest request) {
        try {
            var signedToken = verifyToken(request.getToken(), true);

            String jit = signedToken.getJWTClaimsSet().getJWTID();
            Date expiry = signedToken.getJWTClaimsSet().getExpirationTime();

            invalidTokenRepository.save(new InvalidToken(jit, expiry));
            return null;
        } catch (AppException | JOSEException | ParseException exception) {
            return "You weren't logging in!";
        }
    }

    public AuthenticateResponse refresh(RefreshRequest request) throws ParseException, JOSEException {
        var signedToken = verifyToken(request.getToken(), true);

        var id = signedToken.getJWTClaimsSet().getJWTID();
        var expiry = signedToken.getJWTClaimsSet().getExpirationTime();

        InvalidToken invalidToken = InvalidToken.builder().id(id).expiry(expiry).build();

        invalidTokenRepository.save(invalidToken);

        var username = signedToken.getJWTClaimsSet().getSubject();
        var user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        return AuthenticateResponse.builder()
                .authenticated(true)
                .token(generateToken(user))
                .build();
    }

    public SendEmailResponse isSentResetLink(SendEmailRequest request) {

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        ResetPasswordToken token = ResetPasswordToken.builder()
                .username(user.getUsername())
                .token(UUID.randomUUID().toString())
                .expiry(new Date(Instant.now()
                        .plus(120, ChronoUnit.SECONDS)
                        .toEpochMilli())).build();

        resetPasswordTokenRepository.save(token);

        sendMail(request.getEmail(), user.getUsername(), token.getToken());

        return SendEmailResponse.builder()
                .sent(true)
                .message("Reset link is sent to your email " + request.getEmail()).build();

    }

    public ResetPasswordResponse resetPassword(ResetPasswordRequest request, String token) {
        ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findById(token).orElseThrow(() -> new AppException(ErrorCode.TOKEN_NOT_FOUND));

        if (resetPasswordToken.getExpiry().before(new Date())) throw new AppException(ErrorCode.TOKEN_EXPIRED);

        User user = userRepository.findByUsername(resetPasswordToken.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));

        userRepository.save(user);

        return ResetPasswordResponse.builder()
                .success(true).build();
    }

    void sendMail(String email, String name, String token) {
        SimpleMailMessage message = new SimpleMailMessage();

        String content = "Dear Mr/Ms." +
                name +
                ",\n" +
                "Click on that link to reset your password: http://localhost:8080/auth/forgot/" + token;

        message.setFrom(hostMail);
        message.setSubject("[TripMate's Client Care] Supports Password Recovery");
        message.setText(content);
        message.setTo(email);
        mailSender.send(message);
    }

    SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(signKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(refreshableDuration, ChronoUnit.SECONDS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("PhunTrun")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now()
                        .plus(validationDuration, ChronoUnit.SECONDS)
                        .toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jWSObject = new JWSObject(header, payload);
        try {
            jWSObject.sign(new MACSigner(signKey.getBytes()));
            return jWSObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token!", e);
            throw new RuntimeException(e);
        }
    }

    String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }
}
