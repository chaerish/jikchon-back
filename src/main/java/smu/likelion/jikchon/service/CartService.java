package smu.likelion.jikchon.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.dto.cart.CartProductReturnDto;
import smu.likelion.jikchon.repository.CartRepository;
import smu.likelion.jikchon.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;

    //유저의 장바구니 조회 - 페이징이용
    public PageResult<CartProductReturnDto> getMemberCartList(){
        return null;
    }

    //유저가 프로덕트를 장바구니에 추가
    public void addProductToCart(Long productId,Long quantity){

    }

    //유저가 장바구니에서 프로덕트를 삭제
    public void deleteProductInCart(Long productId){

    }


}
