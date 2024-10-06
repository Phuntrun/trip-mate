package com.exeg2.tripmate.config;

import com.exeg2.tripmate.enums.ErrorCode;
import com.exeg2.tripmate.exception.AppException;
import com.exeg2.tripmate.model.User;
import com.exeg2.tripmate.repository.RoleRepository;
import com.exeg2.tripmate.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    RoleRepository roleRepository;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                var roles = roleRepository.findById("ADMIN").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
                User user = User.builder()
                        .username("admin")
                        .password(new BCryptPasswordEncoder().encode("admin"))
                        .roles(Collections.singleton(roles))
                        .enable(true)
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: admin. please change it!");
            }

            if (userRepository.findByUsername("user").isEmpty()) {
                var roles = roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
                User user = User.builder()
                        .username("user")
                        .password(new BCryptPasswordEncoder().encode("user"))
                        .roles(Collections.singleton(roles))
                        .enable(true)
                        .build();

                userRepository.save(user);
                log.warn("user user has been created with default password: admin. please change it!");
            }
        };
    }
}
