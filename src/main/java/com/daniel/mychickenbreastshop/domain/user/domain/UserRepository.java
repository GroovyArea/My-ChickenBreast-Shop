package com.daniel.mychickenbreastshop.domain.user.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

    Page<User> findAll(final Pageable pageable);

    @Query(value = "SELECT u" +
            " FROM User u" +
            " WHERE u.loginId = :loginId")
    List<User> searchByLoginId(@Param("loginId") String loginId, Pageable pageable);
}
