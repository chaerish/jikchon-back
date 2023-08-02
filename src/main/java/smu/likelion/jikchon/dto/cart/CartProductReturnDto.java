package smu.likelion.jikchon.dto.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import smu.likelion.jikchon.domain.CartProduct;

@Getter
@Setter
@Builder
public class CartProductReturnDto {
    private Long id;
    private String storeName;
    private String address;
    private Long quantity;
    private String imageUrl;
    public CartProductReturnDto CartProductReturnDto (CartProduct cartProduct){
        return CartProductReturnDto.builder()
                .id(cartProduct.getId())
                .storeName(cartProduct.getCart().getMember().getUsername())
                .address(cartProduct.getCart().getMember().getAddress())
                .quantity(cartProduct.getQuantity())
                .imageUrl(cartProduct.getProduct().getImageUrl())
                .build();
    }



}
