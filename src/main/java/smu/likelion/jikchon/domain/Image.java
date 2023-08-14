package smu.likelion.jikchon.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.domain.enumurate.Target;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String imageUrl;
    @Enumerated(EnumType.STRING)
    Target target;
    Long targetId;
    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
    public Image(String imageUrl) {this.imageUrl = imageUrl;}
    public Image(Product product, String url) {
        this.imageUrl=url;
        this.product=product;
    }
}
