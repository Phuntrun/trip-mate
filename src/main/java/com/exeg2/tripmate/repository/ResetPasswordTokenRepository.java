package com.exeg2.tripmate.repository;

import com.exeg2.tripmate.model.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, String> {
}