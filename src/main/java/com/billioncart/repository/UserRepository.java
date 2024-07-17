package com.billioncart.repository;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
}
