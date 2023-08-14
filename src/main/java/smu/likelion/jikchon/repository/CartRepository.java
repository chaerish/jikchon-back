package smu.likelion.jikchon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import smu.likelion.jikchon.domain.Cart;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Page<Cart> findAllByMemberId(Long memberId, Pageable pageable);

    Optional<Cart> findByIdAndMemberId(Long id, Long memberId);

    Optional<Cart> findByMemberIdAndProductId(Long memberId, Long productId);
}