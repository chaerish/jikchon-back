package smu.likelion.jikchon.dto.product;

import lombok.*;
import smu.likelion.jikchon.domain.Product;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class ProductReturnDto {
    @Getter
    @Setter
    @Builder
    public static class Simple { //목록에서 봤을 때 간단하게 보이는것
        private Long productId;
        private String productName;
        private String subCategory;
        private Integer price;
        private String imageUrl;

        public static ProductReturnDto.Simple of(Product product) {
            return Simple.builder()
                    .productId(product.getId())
                    .productName(product.getProductName())
                    .subCategory(product.getSubCategory().getDescription())
                    .price(product.getPrice())
                    .imageUrl(product.getImageList().get(0).getImageUrl())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    public static class Multiple { //목록에서 봤을 때 간단하게 보이는것
        List<Simple> productList;

        public static Multiple of(List<Product> productList) {
            return Multiple.builder()
                    .productList(productList.stream().map(Simple::of).collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    public static class Detail { //상세정보
        private Long productId;
        private String productName;
        private String category;
        private String subCategory;
        private String address;
        private Integer price;
        private Long quantity;
        private List<String> imageUrl;
        private String intro;

        public static Detail of(Product product) {
            return Detail.builder()
                    .productId(product.getId())
                    .productName(product.getProductName())
                    .subCategory(product.getSubCategory().getDescription())
                    .address(product.getMember().getAddress())
                    .price(product.getPrice())
                    .imageUrl(product.getProductImageUrlList())
                    .intro(product.getIntro())
                    .build();
        }
    }
}
