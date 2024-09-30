package com.exeg2.tripmate.repository;

import com.exeg2.tripmate.model.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String> {
}