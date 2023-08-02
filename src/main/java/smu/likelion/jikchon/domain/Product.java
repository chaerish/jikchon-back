package smu.likelion.jikchon.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    @Column(name="productName")
    private String productName;
    @Column(name="productImg")
    private String imageUrl;
    @Column(name="price")
    private Long price;
    @Column(name="intro")
    private String intro;
    @Column(name="quantity")
    private Long quantity;
    @ManyToOne
    @JoinColumn(name="memberId")
    private Member member;
//    @OneToMany
//    @JoinColumn(name="categoryId")
//    private Category categoryId;
    @ManyToOne
    @JoinColumn(name="cartId")
    private Cart cart;

}
