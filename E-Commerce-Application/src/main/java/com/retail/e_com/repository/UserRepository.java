package com.retail.e_com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.retail.e_com.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);
}
