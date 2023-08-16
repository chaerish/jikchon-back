package smu.likelion.jikchon.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import smu.likelion.jikchon.domain.enumurate.SubCategory;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.domain.Product;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProductRequestDto {
    private String productName;
    private String category;
    private Integer price;
    private Long quantity;
    private String intro;

    public Product toEntity(Long memberId) {
        SubCategory subCategory = SubCategory.fromDescription(this.category);
        return Product.builder()
                .productName(this.productName)
                .category(subCategory.getParentCategory())
                .subCategory(subCategory)
                .price(this.price)
                .quantity(this.quantity)
                .intro(this.intro)
                .member(Member.builder().id(memberId).build())//멤버클래스 빌더 객체 생성->id 필드 설정->member객체 생성
                .build();
    }

}
