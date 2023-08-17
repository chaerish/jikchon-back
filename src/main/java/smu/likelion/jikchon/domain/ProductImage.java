package smu.likelion.jikchon.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Entity
@Getter
@DiscriminatorValue("inquiry")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImage extends Image {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    protected ProductImage() {
    }

    @Builder
    public ProductImage(Product product, String imageUrl) {
        super(imageUrl);
        this.product = product;
    }
}
