package smu.likelion.jikchon.dto.purchase;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.domain.Purchase;


public class PurchaseResponseDto {
    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Brief {
        Long id;
        String productName;
        int price;
        int quantity;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String imageUrl;

        public static Brief of(Purchase purchase) {
            return Brief.builder()
                    .id(purchase.getId())
                    .productName(purchase.getProduct().getProductName())
                    .price(purchase.calculatePrice())
                    .quantity(purchase.getQuantity())
                    .build();
        }
    }
}
