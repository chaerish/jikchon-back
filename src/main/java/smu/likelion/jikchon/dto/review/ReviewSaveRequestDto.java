package smu.likelion.jikchon.dto.review;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSaveRequestDto {
    private String content;
    private Integer starRating;
}
