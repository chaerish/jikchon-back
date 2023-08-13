package smu.likelion.jikchon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import smu.likelion.jikchon.base.BaseResponse;
import smu.likelion.jikchon.dto.cart.CartRequestDto;
import smu.likelion.jikchon.dto.purchase.PurchaseDTO;
import smu.likelion.jikchon.service.PurchaseService;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping
    public BaseResponse<Void> purchaseProduct(@RequestBody PurchaseDTO purchaseDTO) {
        purchaseService.purchaseProduct(purchaseDTO);
        return BaseResponse.ok();
    }

    @PostMapping("/cart")
    public BaseResponse<Void> purchaseCartProduct(@RequestBody CartRequestDto cartRequestDTO) {
        purchaseService.purchaseCartProduct(cartRequestDTO);
        return BaseResponse.ok();
    }

    @DeleteMapping("/{purchasesId}")
    public BaseResponse<Void> deleteProduct(@PathVariable("purchasesId") Long purchaseId) {
        purchaseService.deleteProduct(purchaseId);
        return BaseResponse.ok();
    }
}
