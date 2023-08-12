package smu.likelion.jikchon.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smu.likelion.jikchon.domain.Cart;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.domain.Purchase;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.dto.cart.CartRequestDto;
import smu.likelion.jikchon.dto.purchase.PurchaseDTO;
import smu.likelion.jikchon.exception.CustomNotFoundException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.repository.CartRepository;
import smu.likelion.jikchon.repository.MemberRepository;
import smu.likelion.jikchon.repository.ProductRepository;
import smu.likelion.jikchon.repository.PurchaseRepository;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final LoginService loginService;

    //제품 구매
    public void purchaseProduct(PurchaseDTO purchaseDTO) {
        Member member = memberRepository.findById(loginService.getLoginMemberId()).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER);
        });
        Product product = productRepository.findById(purchaseDTO.getId()).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
        long quantity = purchaseDTO.getQuantity();
        if (quantity > product.getQuantity()) {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT); //에러코드 품절로 수정 ?!
        } else {
            Purchase purchase = Purchase.builder()
                    .member(member)
                    .product(product)
                    .quantity(purchaseDTO.getQuantity())
                    .build();
            purchaseRepository.save(purchase);

            product.setQuantity(product.getQuantity() - quantity);
            purchaseRepository.save(purchase);
            productRepository.save(product);
        }
    }

    public void purchaseCartProduct(CartRequestDto cartRequestDTO) { //장바구니 Id, 물건 개수를 받아와서 물건 id를 찾아야함
        Member member = memberRepository.findById(loginService.getLoginMemberId()).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER);
        });
        Cart cart = cartRepository.findById(cartRequestDTO.getCartId()).orElseThrow(() ->
                new CustomNotFoundException(ErrorCode.NOT_FOUND));
        Product product = cart.getProduct();
        long quantity = cartRequestDTO.getQuantity();

        if (cartRequestDTO.getQuantity() == 0) {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT);
        } else if (quantity > cartRequestDTO.getQuantity()) {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT); //이부분 에러코드 수정해야할까요 ?
        } else {
            Purchase purchase = Purchase.builder()
                    .member(member)
                    .product(cart.getProduct())
                    .quantity(cartRequestDTO.getQuantity())
                    .build();

            product.setQuantity(product.getQuantity() - quantity); //전체 제품을 구매한 개수만큼 줄이기

            purchaseRepository.save(purchase);
            productRepository.save(product);
            cartRepository.save(cart);
        }
    }

    public List<Purchase> purchaseList(Member member) {
        return purchaseRepository.findByMemberId(member.getId());
    }

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
