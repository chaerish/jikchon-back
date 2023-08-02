package smu.likelion.jikchon.dto;

import lombok.*;
import smu.likelion.jikchon.domain.Product;

@NoArgsConstructor
@AllArgsConstructor
public class ProductReturnDto {
    @Getter
    @Setter
    @Builder
    public static class Simple{ //목록에서 봤을 때 간단하게 보이는것
        private Long productId;
        private String productName;
        //private Category category;
        private Long price;
        private String imageUrl;
        public static ProductReturnDto.Simple of(Product product){
            return Simple.builder()
                    .productId(product.getId())
                    .productName(product.getProductName())
                    .price(product.getPrice())
                    .imageUrl(product.getImageUrl())
                    .build();
        }

    }
    @Getter
    @Setter
    @Builder
    public static class Detail{ //상세정보
        private Long productId;
        private String productName;
        //private Category category;
        private String address;
        private Long price;
        private Long quantity;
        private String imageUrl;
        private String intro;
//        List <ReviewReturnDto> reviewList;

        public static Detail of(Product product){
            return Detail.builder()
                    .productId(product.getId())
                    .productName(product.getProductName())
//                    .address(product.getMember().getAddress())
                    .price(product.getPrice())
                    .imageUrl(product.getImageUrl())
                    .intro(product.getIntro())
                    .build();
        }

    }
}
