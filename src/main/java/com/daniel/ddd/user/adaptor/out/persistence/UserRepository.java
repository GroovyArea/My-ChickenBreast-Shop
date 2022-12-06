package com.daniel.ddd.user.adaptor.out.persistence;

import com.daniel.mychickenbreastshop.domain.user.model.User;
import com.daniel.mychickenbreastshop.domain.user.model.query.UserCustomQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomQueryRepository {

    Optional<User> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

}
