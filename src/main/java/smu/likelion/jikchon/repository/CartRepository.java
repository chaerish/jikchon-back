package smu.likelion.jikchon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import smu.likelion.jikchon.domain.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Page<Cart> findAllByMemberId(Long memberId,Pageable pageable);
    Optional <Cart> findByMemberIdAndProductId(Long memberId, Long productId);
    Optional <Cart> findByIdAndMemberId(Long id, Long memberId);
    //List<Cart> findById(Long id);
    Optional<Cart> findById(Long id);
    Optional<Cart> findByMember(Long memberId);

}