package smu.likelion.jikchon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.domain.enumurate.Category;
import smu.likelion.jikchon.domain.enumurate.SubCategory;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.dto.product.ProductRequestDto;
import smu.likelion.jikchon.dto.product.ProductReturnDto;
import smu.likelion.jikchon.exception.CustomException;
import smu.likelion.jikchon.exception.CustomForbiddenException;
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

    @Transactional(readOnly = true)
    public ProductReturnDto.Multiple getRecommendProductList() {
        return ProductReturnDto.Multiple.of(productRepository.findAllRecommendProduct(loginService.getLoginMemberId()));
    }


    //todo : 사용자 선택 카테고리 우선 정렬
    @Transactional(readOnly = true)
    public PageResult<ProductReturnDto.Simple> getProductListByCategory(Integer categoryId, Pageable pageable) {
        Page<Product> products;
        if (categoryId < 100) {
            products = productRepository.findAllByCategory(Category.fromId(categoryId), pageable);
        } else {
            products = productRepository.findAllByCategoryAndInterest(SubCategory.fromId(categoryId), pageable);
        }
        Page<ProductReturnDto.Simple> product = products.map(ProductReturnDto.Simple::of);
        return PageResult.ok(product);
    }

    @Transactional(readOnly = true)
    public PageResult<ProductReturnDto.Simple> getAllProduct(Pageable pageable) {
        Page<Product> products = productRepository.findAllByMemberId(loginService.getLoginMemberId(), pageable);
        Page<ProductReturnDto.Simple> product = products.map(ProductReturnDto.Simple::of);
        return PageResult.ok(product);
    }

    @Transactional(readOnly = true)
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
    public void updateProduct(Long id, ProductRequestDto productRequestDto, List<MultipartFile> productImageList) {
        Product product = productRepository.findById(id).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
        if (!product.getMember().getId().equals(loginService.getLoginMemberId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        SubCategory subCategory = SubCategory.fromDescription(productRequestDto.getCategory());
        product.setProductName(productRequestDto.getProductName());
        product.setCategory(subCategory.getParentCategory());
        product.setSubCategory(subCategory);
        product.setPrice(productRequestDto.getPrice());
        product.setQuantity(productRequestDto.getQuantity());
        product.setIntro(productRequestDto.getIntro());

        imageService.deleteImageList(product.getImageList());
        imageService.saveProductImageList(product, productImageList);
    }

    @Transactional
    public void delete(Long id) {
        Product productData = productRepository.findById(id).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
        if (!productData.getMember().getId().equals(loginService.getLoginMemberId())) {
            throw new CustomForbiddenException(ErrorCode.FORBIDDEN);
        }
        imageService.deleteImageList(productData.getImageList());
        productRepository.delete(productData);
    }
}
