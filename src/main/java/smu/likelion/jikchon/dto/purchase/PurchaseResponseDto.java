package smu.likelion.jikchon.dto.purchase;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


public class PurchaseResponseDto {
    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Brief {
        private Long id;
        private String productName;
        private Long price;
        private Long quantity;
        private String imageUrl;
    }
}
