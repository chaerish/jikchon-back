package smu.likelion.jikchon.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.dto.purchase.PurchaseRequestDto;

import java.util.List;

public class OrderRequestDto {
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CartOrder {
        List<PurchaseRequestDto> cartList;
    }
}
