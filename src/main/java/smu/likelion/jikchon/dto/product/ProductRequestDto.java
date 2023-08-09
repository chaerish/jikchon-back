package smu.likelion.jikchon.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import smu.likelion.jikchon.base.Category;
import smu.likelion.jikchon.base.SubCategory;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.domain.Product;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class ProductRequestDto {
    private String productName;
    private String korCategory; //한국어로 입력받는 카테고리
    private SubCategory subCategory; // 이건 카테고리 enum으로(영어로) 변환된 카테고리
    private Long price;
    private Long quantity;
    private String intro;
    private Long memberId; // 판매자의 아이디
    public Product toEntity(){
        SubCategory subCategory=SubCategory.fromDescription(this.korCategory);
        return Product.builder()
                .productName(this.productName)
                .subCategory(subCategory)
                .price(this.price)
                .quantity(this.quantity)
                .intro(this.intro)
                .member(Member.builder().id(memberId).build())//멤버클래스 빌더 객체 생성->id 필드 설정->member객체 생성
                .build();
    }

}
