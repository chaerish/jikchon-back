package smu.likelion.jikchon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import smu.likelion.jikchon.base.BaseResponse;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.dto.order.OrderRequestDto;
import smu.likelion.jikchon.dto.order.OrderResponseDto;
import smu.likelion.jikchon.dto.purchase.PurchaseRequestDto;
import smu.likelion.jikchon.service.OrderService;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {
    private final OrderService orderService;

    @PostMapping
    public BaseResponse<OrderResponseDto.Simple> purchaseProduct(@RequestBody PurchaseRequestDto purchaseRequestDto) {
        return BaseResponse.ok(orderService.purchaseProduct(purchaseRequestDto));
    }

    @PostMapping("/cart")
    public BaseResponse<OrderResponseDto.Simple> purchaseCartProduct(@RequestBody OrderRequestDto.CartOrder orderRequestDto) {
        return BaseResponse.ok(orderService.purchaseCart(orderRequestDto));
    }

    @GetMapping
    public BaseResponse<PageResult<OrderResponseDto.BriefForCustomer>> getMyOrderList(Pageable pageable) {
        return BaseResponse.ok(orderService.getMyOrderList(pageable));
    }
}
