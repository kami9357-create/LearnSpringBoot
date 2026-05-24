package com.devamateur.administrator.identity_service.service;

import com.devamateur.administrator.identity_service.dto.request.UserCreationRequest;
import com.devamateur.administrator.identity_service.dto.request.UserUpdateRequest;
import com.devamateur.administrator.identity_service.dto.response.UserResponse;
import com.devamateur.administrator.identity_service.entity.User;
import com.devamateur.administrator.identity_service.enums.Role;
import com.devamateur.administrator.identity_service.exception.AppException;
import com.devamateur.administrator.identity_service.exception.ErrorCode;
import com.devamateur.administrator.identity_service.mapper.UserMapper;
import com.devamateur.administrator.identity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service // Đánh dấu đây là Bean phụ trách logic nghiệp vụ
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')") // Chỉ cho phép người dùng có vai trò ADMIN truy cập vào phương thức này
    public List<UserResponse> getAllUsers() {
        log.info("Fetching all users from the database");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @PostAuthorize("returnObject.username == authentication.name or hasRole('ADMIN')")
    // Cho phép người dùng truy cập nếu họ đang xem thông tin của chính mình hoặc họ có vai trò ADMIN
    public UserResponse getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);
        User user = getUserEntityById(id);
        return userMapper.toUserResponse(user);
    }

    // Phương thức này cho phép người dùng xem thông tin của chính họ mà không cần phải biết ID, vì nó sẽ lấy thông tin từ SecurityContext
    public UserResponse myProfile() {
        var context = SecurityContextHolder.getContext();
        String username = Objects.requireNonNull(context.getAuthentication()).getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }

    public UserResponse createUser(UserCreationRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = getUserEntityById(id);
        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        User user = getUserEntityById(id);
        userRepository.delete(user);
    }

    private User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

}
