package com.devamateur.administrator.identity_service.configuration;

import com.devamateur.administrator.identity_service.entity.User;
import com.devamateur.administrator.identity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
        // Bean này sẽ chạy khi ứng dụng khởi động, dùng để kiểm tra và tạo user admin nếu chưa tồn tại
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        // Kiểm tra nếu chưa có user admin thì tạo mới
        return args -> {
            if (!userRepository.existsByUsername("admin")) {
                var roles = new HashSet<String>();
                roles.add("ADMIN");

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("Admin user created with username 'admin' and password 'admin'. Please change the password immediately after logging in.");
            }
        };
    }
}
