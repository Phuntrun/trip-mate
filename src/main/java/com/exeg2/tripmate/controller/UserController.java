package com.exeg2.tripmate.controller;

import com.exeg2.tripmate.dto.request.UserCreateRequest;
import com.exeg2.tripmate.dto.request.UserUpdateRequest;
import com.exeg2.tripmate.dto.response.ApiResponse;
import com.exeg2.tripmate.dto.response.UserResponse;
import com.exeg2.tripmate.service.UserService;
import com.exeg2.tripmate.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name="User Controller")
public class UserController {
    UserService userService;
    private final UserServiceImpl userServiceImpl;

    @Operation(summary = "Create user", description = "create new user")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody @Valid UserCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("User created")
                .result(userService.createUser(request))
                .build());
    }

    @Operation(summary = "Get user", description = "find an user by id")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUsers(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .result(userService.findUserById(id))
                .build());
    }

    @Operation(summary = "Get all users", description = "show all users")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                .result(userService.findAllUsers())
                .build());
    }

    @Operation(summary = "Update user", description = "update user by id")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(id, request))
                .build());
    }

    @Operation(summary = "Delete user", description = "delete user by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.builder()
                        .message("User deleted")
                        .build());
    }

    @Operation(summary = "Info", description = "show token owner info")
    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UserResponse>> getMyUsers() {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userServiceImpl.myinfo());
        return ResponseEntity.ok(apiResponse);
    }
}
