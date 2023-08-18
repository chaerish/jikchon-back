package smu.likelion.jikchon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.domain.Cart;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.domain.Purchase;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.dto.purchase.PurchaseResponseDto;
import smu.likelion.jikchon.exception.CustomForbiddenException;
import smu.likelion.jikchon.exception.CustomNotFoundException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.repository.CartRepository;
import smu.likelion.jikchon.repository.MemberRepository;
import smu.likelion.jikchon.repository.ProductRepository;
import smu.likelion.jikchon.repository.PurchaseRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final LoginService loginService;


    @Transactional(readOnly = true)
    public PageResult<PurchaseResponseDto.BriefForSeller> getSaleList(Pageable pageable) {
        return PageResult.ok(
                purchaseRepository.findByMemberId(loginService.getLoginMemberId(), pageable)
                        .map(PurchaseResponseDto.BriefForSeller::of));
    }

    @Transactional(readOnly = true)
    public PurchaseResponseDto.Receipt getReceipt(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(() ->
                new CustomNotFoundException(ErrorCode.NOT_FOUND_PURCHASE)
        );

        Long id = purchase.getProduct().getMember().getId();
        if (!Objects.equals(purchase.getProduct().getMember().getId(), loginService.getLoginMemberId())) {
            throw new CustomForbiddenException(ErrorCode.FORBIDDEN);
        }

        return PurchaseResponseDto.Receipt.of(purchase);
    }

    @Deprecated
    public void deleteProduct(Long purchaseId) {
        Member member = memberRepository.findById(loginService.getLoginMemberId()).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER);
        });
        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT);
        });
        Product product = purchase.getProduct(); //구매 목록에서의 제품
        Long currentQuantity = product.getQuantity(); //총 수량
        Cart cart = cartRepository.findById(purchaseId).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT);
        });
        if (member.equals(purchase.getMember())) {
            purchaseRepository.delete(purchase);
            product.setQuantity(currentQuantity + 1);
            productRepository.save(product);
            if (Objects.equals(cart.getId(), purchaseId)) { //장바구니에 있는 경우
                //장바구니의 개수를 1 증가
//                cart.setQuantity(cart.getQuantity() + 1);
            } else {
                cartRepository.save(cart);
            }
        }

    }
}
