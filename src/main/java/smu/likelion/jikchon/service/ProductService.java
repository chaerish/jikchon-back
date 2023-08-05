package smu.likelion.jikchon.service;

import org.springframework.data.domain.Page;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.domain.Member;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.dto.product.ProductRequestDto;
import smu.likelion.jikchon.dto.product.ProductReturnDto;
import smu.likelion.jikchon.exception.CustomNotFoundException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.repository.MemberRepository;
import smu.likelion.jikchon.repository.ProductRepository;
import org.springframework.data.domain.Pageable;


public class ProductService{
    private ProductRepository productRepository;
    private LoginService loginService;
    private MemberRepository memberRepository;

    public PageResult<ProductReturnDto.Simple> recommendProduct(){

        return null;
    }

    //모든 프로덕트 보여주기(여기서 멤버는 판매자입니다)
    public PageResult<ProductReturnDto.Simple> getAllProduct(Long memberId, Pageable pageable) {
        Page<Product> products=productRepository.findAllByMemberId(memberId, pageable);
        Page<ProductReturnDto.Simple>product=products.map(ProductReturnDto.Simple::of);
        PageResult<ProductReturnDto.Simple> result=PageResult.ok(product);
        return result;
    }
    //프로덕트 상세보기
    public ProductReturnDto.Detail findById(Long id) {
        Product product =productRepository.findById(id).orElseThrow(()->{
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
       return ProductReturnDto.Detail.of(product);
    }

    //프로덕트 등록
    public void save(ProductRequestDto productRequestDto) {
        Member member = memberRepository.findById(loginService.getLoginMemberId()).orElseThrow(()->{
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER);
        });
        productRepository.save(productRequestDto.toEntity(member));
    }
    //프로덕트 수정
    public void update(Long id, ProductRequestDto productRequestDto) {
        Product productData=productRepository.findById(id).orElseThrow(()->{
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
        productData.setProductName(productRequestDto.getProductName());
//        productData.setCategory(productRequestDto.getCategory());
        productData.setPrice(productRequestDto.getPrice());
        productData.setQuantity(productRequestDto.getQuantity());
        productData.setIntro(productRequestDto.getIntro());

    }
    //프로덕트 삭제
    public void delete(Long id) {
        Product productData=productRepository.findById(id).orElseThrow(()->{
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND);
        });
        productRepository.delete(productData);
    }
}
