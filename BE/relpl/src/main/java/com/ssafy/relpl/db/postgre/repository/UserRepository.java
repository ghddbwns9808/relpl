package com.ssafy.relpl.db.postgre.repository;

import com.ssafy.relpl.db.postgre.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserUid(String userUid);
    Optional<User> findByUserNickname(String userNickname);
}

