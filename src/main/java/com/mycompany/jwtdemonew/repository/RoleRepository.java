package com.mycompany.jwtdemonew.repository;

import com.mycompany.jwtdemonew.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
