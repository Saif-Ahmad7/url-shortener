package com.url_shortener_sb.repository;

import com.url_shortener_sb.entity.Role;
import com.url_shortener_sb.utils.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String roles);
}
