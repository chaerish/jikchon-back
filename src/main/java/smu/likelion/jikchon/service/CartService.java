package smu.likelion.jikchon.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.domain.Cart;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.dto.cart.CartReturnDTO;
import smu.likelion.jikchon.exception.CustomNotFoundException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.repository.CartRepository;
import smu.likelion.jikchon.repository.MemberRepository;
import org.springframework.data.domain.Page;
import smu.likelion.jikchon.repository.ProductRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final LoginService loginService;

    //유저의 장바구니 조회 - 페이징이용
    public PageResult<CartReturnDTO> getMemberCartList(Pageable pageable){
        Long id=loginService.getLoginMemberId(); //로그인한 사용자 id
        Page<CartReturnDTO> cartReturnDto=cartRepository.findAllByMemberId(id,pageable).map(CartReturnDTO::toCartReturnDto);
        PageResult<CartReturnDTO> result = PageResult.ok(cartReturnDto);
        return result;
    }

    //유저가 프로덕트를 장바구니에 추가
    public void addProductToCart(Long productId) {
        //현재 로그인한 유저 정보
        Member member = memberRepository.findById(loginService.getLoginMemberId()).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER);
        });
        //추가하고자 하는 프로덕트
        Product product = productRepository.findById(productId).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
        boolean productInCart = false;
        for (Cart cart : member.getCart()) {
            if (cart.getProduct().equals(product)) {
                // 이미 있는 상품이라면 수량만 증가시킴
                cart.addQuantity();
                productInCart = true;
                break;
            }
        }
        if (!productInCart) {
            Cart cart = new Cart(member, product);
            cartRepository.save(cart);
        }
    }
//    public void addProductToCart(Long productId,Long quantity) {
//        //현재 로그인한 유저 정보
//        Member member = memberRepository.findById(loginService.getLoginMemberId()).orElseThrow(() -> {
//            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER);
//        });
//        //추가하고자 하는 프로덕트
//        Product product = productRepository.findById(productId).orElseThrow(() -> {
//            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
//        });
//        boolean productInCart = false;
//        for (Cart cart : member.getCart()) {
//            if (cart.getProduct().equals(product)) {
//                // 이미 있는 상품이라면 수량만 증가시킴
//                cart.setQuantity(cart.getQuantity() + quantity);
//                productInCart = true;
//                break;
//            }
//        }
//        if (!productInCart) {
//            Cart cart = new Cart(member, product, quantity);
//            cartRepository.save(cart);
//        }
//    }
    //유저가 장바구니에서 프로덕트를 삭제
    public void deleteProductInCart(Long cartId){
        Member member=memberRepository.findById(loginService.getLoginMemberId()).orElseThrow(()->{
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER);
        });
        Cart cart =cartRepository.findByIdAndMemberId(cartId,loginService.getLoginMemberId()).orElseThrow(()->{
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
        cartRepository.delete(cart);
    }


}
