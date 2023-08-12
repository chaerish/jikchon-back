package smu.likelion.jikchon.controller;

import org.springframework.web.bind.annotation.*;
import smu.likelion.jikchon.base.BaseResponse;
import smu.likelion.jikchon.dto.cart.CartRequestDTO;
import smu.likelion.jikchon.dto.purchase.PurchaseDTO;
import smu.likelion.jikchon.service.PurchaseService;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {
    private PurchaseService purchaseService;

    @PostMapping("/")
    public BaseResponse<Void> purchaseProduct(@RequestBody PurchaseDTO purchaseDTO){
        purchaseService.purchaseProduct(purchaseDTO);
        return BaseResponse.ok();
    }
    @PostMapping("/cart")
    public BaseResponse<Void> purchaseCartProduct(@RequestBody CartRequestDTO cartRequestDTO){
        purchaseService.purchaseCartProduct(cartRequestDTO);
        return BaseResponse.ok();
    }
    @DeleteMapping("/{purchasesId")
    public BaseResponse<Void> deleteProduct(@PathVariable("purchasesId")Long purchaseId){
        purchaseService.deleteProduct(purchaseId);
        return BaseResponse.ok();
    }
}
