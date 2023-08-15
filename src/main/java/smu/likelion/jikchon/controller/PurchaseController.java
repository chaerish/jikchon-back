package smu.likelion.jikchon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import smu.likelion.jikchon.base.BaseResponse;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.dto.purchase.PurchaseResponseDto;
import smu.likelion.jikchon.service.PurchaseService;

@RestController
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @GetMapping("/seller/purchases")
    @PreAuthorize("hasRole('SELLER')")
    public BaseResponse<PageResult<PurchaseResponseDto.BriefForSeller>> getSaleList(Pageable pageable) {
        return BaseResponse.ok(purchaseService.getSaleList(pageable));
    }

    @GetMapping("/seller/receipt/{purchaseId}")
    @PreAuthorize("hasRole('SELLER')")
    public BaseResponse<PurchaseResponseDto.Receipt> getReceipt(@PathVariable Long purchaseId) {
        return BaseResponse.ok(purchaseService.getReceipt(purchaseId));
    }
}
