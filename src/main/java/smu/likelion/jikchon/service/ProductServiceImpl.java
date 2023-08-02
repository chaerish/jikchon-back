package smu.likelion.jikchon.service;

import org.springframework.data.domain.Page;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.dto.ProductRequestDto;
import smu.likelion.jikchon.dto.ProductReturnDto;
import smu.likelion.jikchon.exception.CustomException;
import smu.likelion.jikchon.exception.CustomNotFoundException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.repository.ProductRepository;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    @Override
    public PageResult<ProductReturnDto.Simple> recommendProduct(){
        return null;
    }

    //모든 프로덕트 보여주기
    @Override
    public PageResult<ProductReturnDto.Simple> getAllProduct(Long memberId, Pageable pageable) {
        Page<ProductReturnDto.Simple> products=productRepository.findAllByMemberId(memberId, pageable);
        PageResult<ProductReturnDto.Simple> result=PageResult.ok(products);
        return result;
    }
    //프로덕트 상세보기
    @Override
    public ProductReturnDto.Detail findById(Long id) {
        Product product =productRepository.findById(id).orElseThrow(()->{
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
       return ProductReturnDto.Detail.of(product);

    }

    //프로덕트 등록
    @Override
    public void save(ProductRequestDto productRequestDto) {
        productRepository.save(productRequestDto.toEntity());
    }
    //프로덕트 수정
    @Override
    public void update(Long id, ProductRequestDto productRequestDto) {
        Product productData=productRepository.findById(id).orElseThrow(()->{
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
        productData.setProductName(productRequestDto.getProductName());
//        productData.setCategory(productRequestDto.getCategory());
        productData.setPrice(productRequestDto.getPrice());
        productData.setQuantity(productRequestDto.getQuantity());
        productData.setIntro(productRequestDto.getIntro());

    }
    //프로덕트 삭제
    @Override
    public void delete(Long id) {
        Product productData=productRepository.findById(id).orElseThrow(()->{
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
        productRepository.delete(productData);
    }
}
