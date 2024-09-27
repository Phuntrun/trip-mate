package com.exeg2.tripmate.service;

import com.exeg2.tripmate.dto.request.UserCreateRequest;
import com.exeg2.tripmate.dto.request.UserUpdateRequest;
import com.exeg2.tripmate.dto.response.UserResponse;
import com.exeg2.tripmate.model.User;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest userCreateRequest);
    UserResponse findUserById(String id);
    List<UserResponse> findAllUsers();
    UserResponse updateUser(String id, UserUpdateRequest request);
    void deleteUser(String id);
}
