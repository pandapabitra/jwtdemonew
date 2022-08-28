package com.mycompany.jwtdemonew.repository;

import com.mycompany.jwtdemonew.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public UserEntity findByUsername(String username);
}
