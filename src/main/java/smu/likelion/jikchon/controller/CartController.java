package smu.likelion.jikchon.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import smu.likelion.jikchon.base.BaseResponse;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.dto.cart.CartReturnDto;
import smu.likelion.jikchon.service.CartService;


@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/members/cart")
    @PreAuthorize("hasRole('CUSTOMER')")
    public BaseResponse<PageResult<CartReturnDto>> getMemberCartList(@PageableDefault(size=12) Pageable pageable){
        return BaseResponse.ok(cartService.getMemberCartList(pageable));
    }

    @PostMapping("products/{productId}/cart")
    @PreAuthorize("hasRole('CUSTOMER')")
    public BaseResponse<Void> addProductToCart(@PathVariable("productId") Long id){
        cartService.addProductToCart(id);
        return BaseResponse.ok();
    }
    @DeleteMapping("members/cart/{cartId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public BaseResponse<Void> deleteProductInCart(@PathVariable("cartId") Long id){
        cartService.deleteProductInCart(id);
        return BaseResponse.ok();
    }
}
