package com.daniel.mychickenbreastshop.domain.user.respository;

import com.daniel.mychickenbreastshop.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
