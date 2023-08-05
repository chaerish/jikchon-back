package smu.likelion.jikchon.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.dto.review.ReviewResponseDTO;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "starRating")
    private Integer starRating;
    @Column(name = "content")
    private String content;
    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;
    @ManyToOne
    @JoinColumn(name="productId")
    private Product product;

    public ReviewResponseDTO toReviewResponseDTO(){
        return new ReviewResponseDTO(
                id,content,starRating,member.getId(),product.getId()
        );
    }

}
