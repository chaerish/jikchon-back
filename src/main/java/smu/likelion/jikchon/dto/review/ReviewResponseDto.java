package smu.likelion.jikchon.dto.review;

import lombok.*;
import smu.likelion.jikchon.domain.Review;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {
    private Long id;
    private String userName;
    private String content;
    private Integer starRating;

    public static List<ReviewResponseDto> fromList(List<Review> reviewList) {
        return reviewList.stream().map(ReviewResponseDto::of).collect(Collectors.toList());
    }

    public static ReviewResponseDto of(Review review) {
        return ReviewResponseDto.builder()
                .id(review.getId())
                .userName(review.getMember().getUsername())
                .content(review.getContent())
                .starRating(review.getStarRating())
                .build();
    }
}
