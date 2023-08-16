package smu.likelion.jikchon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.domain.ProductImage;
import smu.likelion.jikchon.domain.enumurate.SubCategory;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.domain.enumurate.Target;
import smu.likelion.jikchon.dto.product.ProductRequestDto;
import smu.likelion.jikchon.dto.product.ProductReturnDto;
import smu.likelion.jikchon.exception.CustomException;
import smu.likelion.jikchon.exception.CustomNotFoundException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.repository.ProductRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final LoginService loginService;
    private final ImageService imageService;

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

    @Transactional
    public void save(ProductRequestDto productRequestDto, List<MultipartFile> productImageList) {
        Product product = productRepository.save(productRequestDto.toEntity(loginService.getLoginMemberId()));

        imageService.saveProductImageList(product, productImageList);
    }

    @Transactional
    //프로덕트 수정 + 이미지 수정 추가
    public void updateProduct(Long id, ProductRequestDto productRequestDto, List<MultipartFile> productImageList) {
        Product product = productRepository.findById(id).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
        if (!product.getMember().getId().equals(loginService.getLoginMemberId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        //만약 이미지는 수정되지 않았으면
        if(productImageList.isEmpty()){
            product.setProductName(productRequestDto.getProductName());
            product.setSubCategory(SubCategory.fromDescription(productRequestDto.getCategory()));
            product.setPrice(productRequestDto.getPrice());
            product.setQuantity(productRequestDto.getQuantity());
            product.setIntro(productRequestDto.getIntro());
            product.setImageList(product.getImageList());
        }
        //이미지도 수정이 되었으면
        product.setProductName(productRequestDto.getProductName());
        product.setSubCategory(SubCategory.fromDescription(productRequestDto.getCategory()));
        product.setPrice(productRequestDto.getPrice());
        product.setQuantity(productRequestDto.getQuantity());
        product.setIntro(productRequestDto.getIntro());
        imageService.deleteImages(product.getImageList());
        imageService.saveProductImageList(product,productImageList);
    }

    //프로덕트 삭제
    public void delete(Long id) {
        Product productData = productRepository.findById(id).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
        if (!productData.getMember().getId().equals(loginService.getLoginMemberId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        imageService.deleteImages(productData.getImageList());
        productRepository.delete(productData);
    }
}
