package smu.likelion.jikchon.dto.cart;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@Setter
@Builder
public class CartProductRequestDto {
    private Long productId;
    private Long quantity;

}
