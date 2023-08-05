package smu.likelion.jikchon.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.domain.Product;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
public class ProductRequestDto {
    private String productName;

    //private Category category;
    private Long price;
    private Long quantity;
    private String intro;
    private Member member; // 판매자의 아이디

    public Product toEntity(Member member){
        return Product.builder()
                .productName(this.productName)
                .price(this.price)
                .quantity(this.quantity)
                .intro(this.intro)
                .member(member)
                .build();
    }

}
