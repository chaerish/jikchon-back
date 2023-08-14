package smu.likelion.jikchon.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.base.Category;
import smu.likelion.jikchon.base.SubCategory;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.exception.CustomBadRequestException;
import smu.likelion.jikchon.exception.ErrorCode;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String productName;
    private String imageUrl;
    private Integer price;
    private String intro;
    private Long quantity;
    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    private SubCategory subCategory;
    @OneToMany(mappedBy = "product")
    private List<Cart> cart;
    @OneToMany(mappedBy = "product")
    private List<Purchase> purchaseList;

    public void reduceQuantity(int purchaseQuantity) {
        if (quantity - purchaseQuantity < 0) {
            throw new CustomBadRequestException(ErrorCode.OUT_OF_STOCK);
        }
        this.quantity -= purchaseQuantity;
    }
}
