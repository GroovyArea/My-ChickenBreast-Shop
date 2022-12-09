package com.daniel.mychickenbreastshop.user.adaptor.out.persistence;

import com.daniel.mychickenbreastshop.user.adaptor.out.persistence.query.UserCustomQueryRepository;
import com.daniel.mychickenbreastshop.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomQueryRepository {

    Optional<User> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

}
