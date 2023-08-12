package smu.likelion.jikchon.dto.cart;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartRequestDTO {
    private Long cartId;
    private Long quantity;
}
