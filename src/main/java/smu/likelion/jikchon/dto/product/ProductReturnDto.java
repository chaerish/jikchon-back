package smu.likelion.jikchon.dto.product;

import lombok.*;
import smu.likelion.jikchon.base.Category;
import smu.likelion.jikchon.domain.Product;

@NoArgsConstructor
public class ProductReturnDto {
    @Getter
    @Setter
    @Builder
    public static class Simple{ //목록에서 봤을 때 간단하게 보이는것
        private Long productId;
        private String productName;
        private String subCategory;
        private Long price;
        private String imageUrl;
        public static ProductReturnDto.Simple of(Product product){
            return Simple.builder()
                    .productId(product.getId())
                    .productName(product.getProductName())
                    .subCategory(product.getSubCategory().getDescription())
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
        private String category;
        private String subCategory;
        private String address;
        private Long price;
        private Long quantity;
        private String imageUrl;
        private String intro;

        public static Detail of(Product product){
            return Detail.builder()
                    .productId(product.getId())
                    .productName(product.getProductName())
                    .subCategory(product.getSubCategory().getDescription())
                    .address(product.getMember().getAddress())
                    .price(product.getPrice())
                    .imageUrl(product.getImageUrl())
                    .intro(product.getIntro())
                    .build();
        }

    }
}
