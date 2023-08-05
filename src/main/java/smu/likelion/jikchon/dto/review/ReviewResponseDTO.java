package smu.likelion.jikchon.dto.review;

import lombok.*;
import smu.likelion.jikchon.domain.Member;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.domain.Review;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDTO {
    private Long id;
    private String content;
    private Integer starRating;
    private Long member;
    private Long product;
    public ReviewResponseDTO(Review review){
     id = review.getId();
     content = review.getContent();
     starRating = review.getStarRating();
     member = review.getMember().getId();
     product = review.getMember().getId();
    }
}
