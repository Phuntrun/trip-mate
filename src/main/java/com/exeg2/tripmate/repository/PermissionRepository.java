package com.exeg2.tripmate.repository;

import com.exeg2.tripmate.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}