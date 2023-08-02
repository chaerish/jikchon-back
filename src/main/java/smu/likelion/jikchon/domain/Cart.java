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
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name="quantity")
    private Long quantity;

    @OneToOne
    @JoinColumn(name="memberId")
    private Member member;

    public static Cart createCart(Member member){
        Cart cart=new Cart();
        cart.setMember(member);
        return cart;
    }


}
