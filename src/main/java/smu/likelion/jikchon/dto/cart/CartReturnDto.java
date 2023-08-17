package smu.likelion.jikchon.dto.cart;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.domain.Cart;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartReturnDto {
    Long id;
    String storeName;
    String productName;
    String address;
    Integer price;
    String imageUrl;

    public static CartReturnDto toCartReturnDto(Cart cart){
        return CartReturnDto.builder()
                .id(cart.getId())
                .storeName(cart.getProduct().getMember().getUsername())
                .productName(cart.getProduct().getProductName())
                .price(cart.getProduct().getPrice())
                .address(cart.getProduct().getMember().getAddress())
                .imageUrl(cart.getProduct().getImageList().get(0).getImageUrl())
                .build();
    }
}
