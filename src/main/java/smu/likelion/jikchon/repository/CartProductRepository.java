package smu.likelion.jikchon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smu.likelion.jikchon.domain.CartProduct;

public interface CartProductRepository extends JpaRepository<CartProduct,Long> {
    CartProduct findByCartIdAndProductId(Long cartId,Long productId);
}
