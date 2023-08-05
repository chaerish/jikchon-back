package smu.likelion.jikchon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smu.likelion.jikchon.domain.member.VerifiedMember;

import java.util.Optional;

public interface VerifiedCacheRepository extends JpaRepository<VerifiedMember, Long> {
    Optional<VerifiedMember> findByPhoneNumber(String phoneNumber);
}
