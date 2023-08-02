package smu.likelion.jikchon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.dto.product.ProductReturnDto;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<ProductReturnDto.Simple> findAllByMemberId(Long memberId, Pageable pageable);


}
