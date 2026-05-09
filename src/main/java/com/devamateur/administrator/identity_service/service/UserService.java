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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        User user = getUserEntityById(id);
        return userMapper.toUserResponse(user);
    }

    public UserResponse createUser(UserCreationRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

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
