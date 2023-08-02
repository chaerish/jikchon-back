package smu.likelion.jikchon.service;

import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.dto.product.ProductRequestDto;
import smu.likelion.jikchon.dto.product.ProductReturnDto;

import org.springframework.data.domain.Pageable;

public interface ProductService{

    //사용자 맞춤 프로덕트
    PageResult<ProductReturnDto.Simple> recommendProduct();
    //가게의 프로덕트 전체 보기
    PageResult<ProductReturnDto.Simple> getAllProduct(Long memberId, Pageable pageable);
    //특정 프로덕트 상세보기
    ProductReturnDto.Detail findById(Long id);
    //프로덕트 등록하기
    void save(ProductRequestDto productRequestDto);
    //프로덕트 수정하기
    void update(Long id, ProductRequestDto productRequestDto);
    //프로덕트 삭제하기
    void delete(Long id);

}
