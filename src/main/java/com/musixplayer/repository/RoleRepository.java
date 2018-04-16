package com.musixplayer.repository;

import com.musixplayer.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT u FROM Role u WHERE u.name =:role_name")
    Optional<Role> findByRoleName(@Param("role_name") String role_name);
}
