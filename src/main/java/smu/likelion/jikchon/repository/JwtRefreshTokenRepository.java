package smu.likelion.jikchon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smu.likelion.jikchon.domain.JwtRefreshToken;

import java.util.Optional;

public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, Long> {
    Optional<JwtRefreshToken> findByMemberId(Long memberId);
}
