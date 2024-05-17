package com.retail.e_com.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.retail.e_com.model.RefreshToken;
import com.retail.e_com.model.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>{

	boolean existsByTokenAndIsBlocked(String rt, boolean b);

	Optional<RefreshToken> findByToken(String refreshToken);

	List<RefreshToken> findAllByExpirationBefore(LocalDateTime now);

}
