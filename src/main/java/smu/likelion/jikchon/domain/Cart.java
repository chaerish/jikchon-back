package smu.likelion.jikchon.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.domain.member.Member;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "quantity")
    private long quantity;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    public Cart(Member member,Product product){
        this.member=member;
        this.product=product;
        this.quantity= 1;
    }
//    public void addProductToCart(Member member,Product product) {
//        // 이미 해당 상품이 장바구니에 있는지 체크
//        for (Cart cart : member.getCart()) {
//            if (cart.getProduct().equals(product)) {
//                // 이미 있는 상품이라면 수량만 증가시킴
//                cart.setQuantity(cart.getQuantity() + quantity);
//            }
//        }
//        // 장바구니에 없는 상품이라면 새로운 장바구니 항목 생성
//        Cart newCartItem = new Cart(member,product,quantity);
//    }
    // 수량 증가
    public void addQuantity() {
        this.quantity++;
    }
}