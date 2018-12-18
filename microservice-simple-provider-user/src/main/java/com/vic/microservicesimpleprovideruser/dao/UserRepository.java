package com.vic.microservicesimpleprovideruser.dao;

import com.vic.microservicesimpleprovideruser.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
