package smu.likelion.jikchon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import smu.likelion.jikchon.domain.Cart;
import smu.likelion.jikchon.domain.Member;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Page<Cart> findAllByMemberId(Long memberId,Pageable pageable);
}
