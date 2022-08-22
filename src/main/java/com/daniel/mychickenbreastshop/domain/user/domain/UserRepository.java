package com.daniel.mychickenbreastshop.domain.user.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    Page<User> findAll(final Pageable pageable);
    List<User> findByLoginIdContaining(String loginId, Pageable pageable);
}
