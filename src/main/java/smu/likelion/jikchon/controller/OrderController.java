package smu.likelion.jikchon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import smu.likelion.jikchon.base.BaseResponse;
import smu.likelion.jikchon.dto.order.OrderResponseDto;
import smu.likelion.jikchon.service.OrderService;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/customer/receipt/{orderId}")
    public BaseResponse<OrderResponseDto.Receipt> getOrderReceipt(@PathVariable Long orderId) {
        return BaseResponse.ok(orderService.getOrderReceipt(orderId));
    }
}
