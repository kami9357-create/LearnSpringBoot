package com.devamateur.administrator.identity_service.repository;

import com.devamateur.administrator.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Đánh dấu đây là một Bean phụ trách giao tiếp với CSDL
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
