package com.exeg2.tripmate.controller;

import com.exeg2.tripmate.dto.request.PermissionRequest;
import com.exeg2.tripmate.dto.request.RoleRequest;
import com.exeg2.tripmate.dto.response.ApiResponse;
import com.exeg2.tripmate.dto.response.PermissionResponse;
import com.exeg2.tripmate.dto.response.RoleResponse;
import com.exeg2.tripmate.service.PermissionService;
import com.exeg2.tripmate.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class RoleController {
    RoleService roleService;
    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(@RequestBody RoleRequest request) {
        return ResponseEntity.ok(ApiResponse.<RoleResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Role created")
                .result(roleService.create(request))
                .build());
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse<RoleResponse>> findByName(@PathVariable String name) {
        return ResponseEntity.ok(ApiResponse.<RoleResponse>builder()
                .result(roleService.findOne(name))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.findAll())
                .build());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String name) {
        roleService.delete(name);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Role deleted")
                .build());
    }
}
