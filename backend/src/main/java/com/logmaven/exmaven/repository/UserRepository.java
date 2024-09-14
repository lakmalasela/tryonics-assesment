package com.logmaven.exmaven.repository;

import com.logmaven.exmaven.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    @Query("select u from User  u where u.username=?1") //jpa query
    User findUserByUsername(String username);

    Optional<User> findByUsername(String username);
}
