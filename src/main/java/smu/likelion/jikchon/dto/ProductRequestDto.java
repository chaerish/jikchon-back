package smu.likelion.jikchon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    public Product toEntity(){
        return Product.builder()
                .productName(this.productName)
                .price(this.price)
                .quantity(this.quantity)
                .intro(this.intro)
                .build();
    }

}
