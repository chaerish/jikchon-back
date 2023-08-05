package smu.likelion.jikchon.dto.cart;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import smu.likelion.jikchon.domain.Cart;

@Getter
@Setter
@Builder
public class CartReturnDto {
    private Long id;
    private String storeName;
    private String address;
    private Integer quantity;
    private String imageUrl;
    public static CartReturnDto toCartReturnDto(Cart cart){
        return CartReturnDto.builder()
                .id(cart.getId())
                .storeName(cart.getMember().getUsername())
                .address(cart.getMember().getAddress())
                .quantity(cart.getQuantity())
                .imageUrl(cart.getProduct().getImageUrl())
                .build();
    }



}
