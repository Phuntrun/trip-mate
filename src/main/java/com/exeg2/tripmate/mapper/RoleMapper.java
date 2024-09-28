package com.exeg2.tripmate.mapper;

import com.exeg2.tripmate.dto.request.PermissionRequest;
import com.exeg2.tripmate.dto.request.RoleRequest;
import com.exeg2.tripmate.dto.response.PermissionResponse;
import com.exeg2.tripmate.dto.response.RoleResponse;
import com.exeg2.tripmate.model.Permission;
import com.exeg2.tripmate.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
