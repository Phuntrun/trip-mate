package com.exeg2.tripmate.service;

import com.exeg2.tripmate.dto.request.UserCreateRequest;
import com.exeg2.tripmate.dto.request.UserUpdateRequest;
import com.exeg2.tripmate.dto.response.UserResponse;
import com.exeg2.tripmate.enums.ErrorCode;
import com.exeg2.tripmate.exception.AppException;
import com.exeg2.tripmate.mapper.UserMapper;
import com.exeg2.tripmate.model.User;
import com.exeg2.tripmate.repository.RoleRepository;
import com.exeg2.tripmate.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserResponse createUser(UserCreateRequest userCreateRequest) {
        if (userRepository.existsByUsername(userCreateRequest.getUsername())) throw new AppException(ErrorCode.USERNAME_USED);
        if (userRepository.existsByEmail(userCreateRequest.getEmail())) throw new AppException(ErrorCode.EMAIL_USED);
        if (userRepository.existsByPhone(userCreateRequest.getPhone())) throw new AppException(ErrorCode.PHONE_USED);

        User user = userMapper.toUser(userCreateRequest);
        user.setEnabled(true);
        var role = roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.setRoles(Collections.singleton(role));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse findUserById(String id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    public List<UserResponse> findAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, request);
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) throw new AppException(ErrorCode.USER_NOT_FOUND);
        userRepository.deleteById(id);
    }
}
