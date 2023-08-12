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
@Setter
@Getter
public class ProductRequestDto {
    private String productName;
    private String subCategory;
    private Long price;
    private Long quantity;
    private String intro;

    public Product toEntity(Long memberId) {
        SubCategory subCategory = SubCategory.fromDescription(this.subCategory);
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