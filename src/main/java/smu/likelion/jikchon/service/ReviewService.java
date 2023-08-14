package smu.likelion.jikchon.service;

import org.springframework.data.domain.Pageable;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.dto.review.ReviewResponseDto;
import smu.likelion.jikchon.dto.review.ReviewSaveRequestDto;

public interface ReviewService {

    //리뷰 등록
    void saveReview(Long productId, ReviewSaveRequestDto reviewSaveRequestDTO);

    //리뷰 수정
    Long updateReview(Long productId, ReviewSaveRequestDto reviewSaveRequestDTO);

    //리뷰 삭제
    void deleteReview(Long reviewId);

    //도움 필요 ,,
    PageResult<ReviewResponseDto> getReviewList(Long productId, Pageable pageable);
}
