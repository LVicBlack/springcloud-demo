package com.vic.microserviceprovideruserwithauth.dao;

import com.vic.microserviceprovideruserwithauth.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
