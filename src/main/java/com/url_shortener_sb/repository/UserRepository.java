package com.url_shortener_sb.repository;

import com.url_shortener_sb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
    @Query(value = "SELECT * FROM users WHERE user_name = :username OR email = :email ", nativeQuery = true)
    Optional<User> findByUsernameOrEmail(@Param("username") String username,@Param("email") String email);
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
