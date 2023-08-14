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
@Table(name = "purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    //물품, 회원
    @Column(name = "quantity")
    Integer quantity;
    Integer price;

    @Column(name = "status")
    Long status;

    @ManyToOne
    @JoinColumn(name = "product_id")
    Member member;

    @ManyToOne
    @JoinColumn(name = "member_id")
    Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    Order order;

    public int calculatePrice() {
        return price * quantity;
    }
}