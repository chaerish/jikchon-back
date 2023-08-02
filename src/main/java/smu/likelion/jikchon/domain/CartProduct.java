package smu.likelion.jikchon.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CartProduct")
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @Column(name="cartId")
    private Cart cart;
    @ManyToOne
    @Column(name="productId")
    private Product product;

    private int count;

}
