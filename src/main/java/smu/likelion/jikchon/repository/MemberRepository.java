package smu.likelion.jikchon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smu.likelion.jikchon.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByPhoneNumber(String phoneNumber);
}
