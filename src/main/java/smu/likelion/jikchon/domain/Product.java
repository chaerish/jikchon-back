package smu.likelion.jikchon.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.base.Category;
import smu.likelion.jikchon.base.SubCategory;
import smu.likelion.jikchon.domain.member.Member;

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
    @Enumerated(EnumType.STRING)
    private Category category;
    @Enumerated(EnumType.STRING)
    private SubCategory subCategory;
    @OneToMany(mappedBy = "product")
    private List<Cart> cart;
}
