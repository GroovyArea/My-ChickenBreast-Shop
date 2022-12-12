package com.daniel.mychickenbreastshop.user.adaptor.out.persistence;

import com.daniel.mychickenbreastshop.user.adaptor.out.persistence.query.UserCustomQueryRepository;
import com.daniel.mychickenbreastshop.user.domain.User;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomQueryRepository {

    Optional<User> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

    Page<User> findAllByRole(Pageable pageable, Role role);

}
