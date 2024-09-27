package com.exeg2.tripmate.mapper;

import com.exeg2.tripmate.dto.request.UserCreateRequest;
import com.exeg2.tripmate.dto.request.UserUpdateRequest;
import com.exeg2.tripmate.dto.response.UserResponse;
import com.exeg2.tripmate.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.TargetType;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest request);

    UserResponse toUserResponse(User user);
    @Mapping(target = "id", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
