package com.devamateur.administrator.identity_service.controller;

import com.devamateur.administrator.identity_service.dto.response.ApiResponse;
import com.devamateur.administrator.identity_service.dto.request.UserCreationRequest;
import com.devamateur.administrator.identity_service.dto.request.UserUpdateRequest;
import com.devamateur.administrator.identity_service.dto.response.UserResponse;
import com.devamateur.administrator.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/identity/users")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Authenticated user: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info("Authority: {}", grantedAuthority.getAuthority()));

        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.getAllUsers());
        return apiResponse;
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.getUserById(id));
        return apiResponse;
    }

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        UserResponse createdUser = userService.createUser(request);
        apiResponse.setData(createdUser);
        return apiResponse;
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(userService.updateUser(id, request));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setData("User with id " + id + " has been deleted.");
        return apiResponse;
    }
}
