package com.exeg2.tripmate.controller;

import com.exeg2.tripmate.dto.request.PermissionRequest;
import com.exeg2.tripmate.dto.response.ApiResponse;
import com.exeg2.tripmate.dto.response.PermissionResponse;
import com.exeg2.tripmate.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class PermissonController {
    PermissionService permissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponse>> create(@RequestBody PermissionRequest request) {
        return ResponseEntity.ok(ApiResponse.<PermissionResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Permission created")
                .result(permissionService.create(request))
                .build());
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse<PermissionResponse>> findByName(@PathVariable String name) {
        return ResponseEntity.ok(ApiResponse.<PermissionResponse>builder()
                .result(permissionService.findByName(name))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.findAll())
                .build());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String name) {
        permissionService.delete(name);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Permission deleted")
                .build());
    }
}
