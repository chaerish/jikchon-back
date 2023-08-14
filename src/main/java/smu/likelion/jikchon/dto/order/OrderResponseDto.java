package smu.likelion.jikchon.dto.order;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.domain.Order;
import smu.likelion.jikchon.domain.Purchase;
import smu.likelion.jikchon.domain.member.Member;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
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
        private Long id;
        private Integer price;
        private String orderDate;
        private List<String> imageUrlList;

        public static BriefForCustomer of(Order order) {
            int price = 0;
            for (Purchase purchase : order.getPurchaseList()) {
                price += purchase.calculatePrice();
            }

            return BriefForCustomer.builder()
                    .id(order.getId())
                    .price(price)
                    .orderDate(order.getCreatedAtToString())
                    //todo : imageUrlList
                    .build();
        }
    }
}
