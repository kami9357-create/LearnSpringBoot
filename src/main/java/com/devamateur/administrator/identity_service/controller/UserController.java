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
// Đánh dấu class này là một REST controller, Spring sẽ tự động chuyển đổi các đối tượng trả về thành JSON
@RequestMapping("/users") //Mọi API endpoint trong controller này sẽ bắt đầu bằng "/identity/users"
@RequiredArgsConstructor  // Tự động sinh constructor với tất cả các field được đánh dấu là final (hoặc @NonNull)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        // Lấy thông tin người dùng hiện tại từ SecurityContext để log ra tên và vai trò của họ
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("User: {}", authentication.getName());
        log.info("Roles: {}", authentication.getAuthorities());

        return ApiResponse.<List<UserResponse>>builder()
                .data(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.getUserById(id))
                .build();
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> myProfile() {
        return ApiResponse.<UserResponse>builder()
                .data(userService.myProfile())
                .build();
    }

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.createUser(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.updateUser(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
