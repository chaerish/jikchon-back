package smu.likelion.jikchon.dto.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.domain.Order;
import smu.likelion.jikchon.domain.Purchase;
import smu.likelion.jikchon.dto.purchase.PurchaseResponseDto;

import java.util.ArrayList;
import java.util.List;

public class OrderResponseDto {
    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Simple {
        Long id;

        public static Simple of(Order order) {
            return Simple.builder()
                    .id(order.getId())
                    .build();
        }
    }

    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class BriefForCustomer {
        Long id;
        Integer price;
        String orderDate;
        List<String> imageUrlList;

        public static BriefForCustomer of(Order order) {
            int price = 0;
            for (Purchase purchase : order.getPurchaseList()) {
                price += purchase.calculatePrice();
            }

            return BriefForCustomer.builder()
                    .id(order.getId())
                    .price(price)
                    .orderDate(order.getCreatedAtToString())
                    .imageUrlList(order.getProductImage(order.getPurchaseList()))
                    .build();
        }
    }

    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Receipt {
        Long orderId;
        Integer totalPrice;
        List<PurchaseResponseDto.Base> productList;

        public static Receipt of(Order order) {
            int totalPrice = 0;
            List<PurchaseResponseDto.Base> purchaseList = new ArrayList<>();

            for (Purchase purchase : order.getPurchaseList()) {
                totalPrice += purchase.calculatePrice();
                purchaseList.add(PurchaseResponseDto.Base.of(purchase));
            }

            return Receipt.builder()
                    .orderId(order.getId())
                    .totalPrice(totalPrice)
                    .productList(purchaseList)
                    .build();
        }
    }
}
