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

    private Long quantity;
    public CartProduct createCartProduct(Cart cart,Product product,Long quantity){
        CartProduct cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(quantity);
        return cartProduct;
    }
    //수량 증가
    public void addQuantity(Long quantity){
        this.quantity+=quantity;
    }



}
