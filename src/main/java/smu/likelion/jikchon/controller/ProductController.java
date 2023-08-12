package smu.likelion.jikchon.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import smu.likelion.jikchon.base.BaseResponse;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.dto.product.ProductRequestDto;
import smu.likelion.jikchon.dto.product.ProductReturnDto;
import smu.likelion.jikchon.service.LoginService;
import smu.likelion.jikchon.service.ProductService;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final LoginService loginService;
    //사용자 추천 프로덕트 보기
//    @GetMapping("/home/products")
//    public BaseResponse<PageResult<ProductReturnDto.Simple>> recommendProduct() {
//        return BaseResponse.ok("아직 구현 안 됨 ");
//    }

    @GetMapping("/products")
    public BaseResponse<PageResult<ProductReturnDto.Simple>> getSellerProduct(
            @RequestParam(required = false, value = "category", defaultValue = "") Integer categoryId,
            @PageableDefault(size = 12) Pageable pageable) {
        return BaseResponse.ok(productService.getProductByCategory(categoryId, pageable));
    }

    //프로덕트 목록 조회
    @GetMapping("/members/products")
    public BaseResponse<PageResult<ProductReturnDto.Simple>> getSellerProduct(@PageableDefault(size = 12) Pageable pageable) {
        return BaseResponse.ok(productService.getAllProduct(pageable));
    }

    //프로덕트 상세 조회
    @GetMapping("/products/{productId}")
    public BaseResponse<ProductReturnDto.Detail> getDetailProduct(@PathVariable("productId") Long id) {
        return BaseResponse.ok(productService.findById(id));
    }

    //프로덕트 등록
    @PostMapping("/products")
    public BaseResponse<Void> registerProduct(@RequestBody ProductRequestDto productRequestDto) {
        productService.save(productRequestDto);
        return BaseResponse.ok();
    }

    //프로덕트 수정
    @PutMapping("/products/{productId}")
    public BaseResponse<Void> updateProduct(@PathVariable("productId") Long id, @RequestBody ProductRequestDto productRequestDto) {
        productService.update(id, productRequestDto);
        return BaseResponse.ok();
    }

    //프로덕트 삭제
    @DeleteMapping("/products/{productId}")
    public BaseResponse<Void> deleteProduct(@PathVariable("productId") Long id) {
        productService.delete(id);
        return BaseResponse.ok();
    }
}
