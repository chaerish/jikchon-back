package smu.likelion.jikchon.dto.purchase;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import smu.likelion.jikchon.domain.Purchase;
import smu.likelion.jikchon.dto.member.MemberResponseDto;

import java.util.List;


public class PurchaseResponseDto {
    @Getter
    @SuperBuilder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Base {
        Long id;
        String productName;
        int price;
        int quantity;

        public static Base of(Purchase purchase) {
            return Base.builder()
                    .id(purchase.getId())
                    .productName(purchase.getProduct().getProductName())
                    .price(purchase.calculatePrice())
                    .quantity(purchase.getQuantity())
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class BriefForSeller {
        Long orderId;
        Long purchaseId;
        String productName;
        int quantity;
        int totalPrice;
        String orderAt;
        List<String> imageUrl;
        MemberResponseDto.Profile purchaseCustomer;

        public static BriefForSeller of(Purchase purchase) {
            return BriefForSeller.builder()
                    .orderId(purchase.getOrder().getId())
                    .purchaseId(purchase.getId())
                    .productName(purchase.getProduct().getProductName())
                    .quantity(purchase.getQuantity())
                    .totalPrice(purchase.calculatePrice())
                    .orderAt(purchase.getOrder().getCreatedAtToString())
                    .imageUrl(purchase.getProduct().getProductImageUrlList())
                    .purchaseCustomer(MemberResponseDto.Profile.of(purchase.getMember()))
                    .build();
        }
    }

    @Getter
    @SuperBuilder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Receipt {
        Long orderId;
        MemberResponseDto.Profile customerDto;
        PurchaseResponseDto.Base purchaseDto;

        public static Receipt of(Purchase purchase) {
            return Receipt.builder()
                    .orderId(purchase.getOrder().getId())
                    .customerDto(MemberResponseDto.Profile.of(purchase.getMember()))
                    .purchaseDto(Base.of(purchase))
                    .build();
        }
    }
}
