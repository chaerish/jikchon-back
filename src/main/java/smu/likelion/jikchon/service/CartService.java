package smu.likelion.jikchon.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smu.likelion.jikchon.repository.CartRepository;
import smu.likelion.jikchon.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;

    //유저 장바구니 조회
    public Page



}
