package com.exeg2.tripmate.service;

import com.exeg2.tripmate.dto.request.PermissionRequest;
import com.exeg2.tripmate.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse create(PermissionRequest request);
    PermissionResponse findByName(String name);
    List<PermissionResponse> findAll();
    void delete(String name);
}
