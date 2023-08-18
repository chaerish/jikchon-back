package smu.likelion.jikchon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import smu.likelion.jikchon.base.BaseResponse;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.dto.order.OrderRequestDto;
import smu.likelion.jikchon.dto.order.OrderResponseDto;
import smu.likelion.jikchon.dto.purchase.PurchaseRequestDto;
import smu.likelion.jikchon.dto.purchase.PurchaseResponseDto;
import smu.likelion.jikchon.service.OrderService;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/purchases")
    @PreAuthorize("isAuthenticated()")
    public BaseResponse<OrderResponseDto.Simple> purchaseProduct(@RequestBody PurchaseRequestDto purchaseRequestDto) {
        return BaseResponse.ok(orderService.purchaseProduct(purchaseRequestDto));
    }

    @PostMapping("/purchases/cart")
    @PreAuthorize("hasRole('CUSTOMER')")
    public BaseResponse<OrderResponseDto.Simple> purchaseCartProduct(@RequestBody OrderRequestDto.CartOrder orderRequestDto) {
        return BaseResponse.ok(orderService.purchaseCart(orderRequestDto));
    }

    @GetMapping("/customer/receipt/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public BaseResponse<OrderResponseDto.Receipt> getOrderReceipt(@PathVariable Long orderId) {
        return BaseResponse.ok(orderService.getOrderReceipt(orderId));
    }

    @GetMapping("/customer/purchases")
    @PreAuthorize("hasRole('CUSTOMER')")
    public BaseResponse<PageResult<OrderResponseDto.BriefForCustomer>> getMyOrderList(Pageable pageable) {
        return BaseResponse.ok(orderService.getMyOrderList(pageable));
    }


}
