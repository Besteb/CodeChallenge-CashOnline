package com.cash_online.Code_Challenge.repositories;

import com.cash_online.Code_Challenge.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);
}
