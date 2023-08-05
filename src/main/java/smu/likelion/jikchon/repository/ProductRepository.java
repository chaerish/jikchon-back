package smu.likelion.jikchon.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.dto.product.ProductReturnDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long productId);
    List<Product> findReviewsByProductId(Long productId);
    Page<Product> findAllByMemberId(Long memberId, Pageable pageable);
}
