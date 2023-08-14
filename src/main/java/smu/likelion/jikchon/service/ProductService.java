package smu.likelion.jikchon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.base.SubCategory;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.dto.product.ProductRequestDto;
import smu.likelion.jikchon.dto.product.ProductReturnDto;
import smu.likelion.jikchon.exception.CustomException;
import smu.likelion.jikchon.exception.CustomNotFoundException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.repository.ProductRepository;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final LoginService loginService;

    public ProductReturnDto.Multiple getRecommendProductList() {
        return ProductReturnDto.Multiple.of(productRepository.findAllRecommendProduct(loginService.getLoginMemberId()));
    }

    //todo : 사용자 선택 카테고리 우선 정렬
    public PageResult<ProductReturnDto.Simple> getProductListByCategory(Integer subCategoryId, Pageable pageable) {
        Page<Product> products = productRepository.findAllByCategoryAndInterest(SubCategory.fromId(subCategoryId), pageable);
        Page<ProductReturnDto.Simple> product = products.map(ProductReturnDto.Simple::of);
        return PageResult.ok(product);
    }

    //모든 프로덕트 보여주기(여기서 멤버는 판매자입니다)
    public PageResult<ProductReturnDto.Simple> getAllProduct(Pageable pageable) {
        Page<Product> products = productRepository.findAllByMemberId(loginService.getLoginMemberId(), pageable);
        Page<ProductReturnDto.Simple> product = products.map(ProductReturnDto.Simple::of);
        return PageResult.ok(product);
    }

    //프로덕트 상세보기
    public ProductReturnDto.Detail findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
        return ProductReturnDto.Detail.of(product);
    }

    //프로덕트 등록
    public void save(ProductRequestDto productRequestDto) {
        productRepository.save(productRequestDto.toEntity(loginService.getLoginMemberId()));
    }

    //프로덕트 수정
    public void update(Long id, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(id).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
        if (!product.getMember().getId().equals(loginService.getLoginMemberId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        product.setProductName(productRequestDto.getProductName());
        product.setSubCategory(SubCategory.fromDescription(productRequestDto.getCategory()));
        product.setPrice(productRequestDto.getPrice());
        product.setQuantity(productRequestDto.getQuantity());
        product.setIntro(productRequestDto.getIntro());
    }

    //프로덕트 삭제
    public void delete(Long id) {
        Product productData = productRepository.findById(id).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
        if (!productData.getMember().getId().equals(loginService.getLoginMemberId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        productRepository.delete(productData);
    }
}
