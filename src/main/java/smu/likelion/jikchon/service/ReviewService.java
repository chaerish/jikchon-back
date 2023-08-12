package smu.likelion.jikchon.service;

import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.dto.review.ReviewResponseDto;
import smu.likelion.jikchon.dto.review.ReviewSaveRequestDTO;

public interface ReviewService {
    //리뷰 전체 조회
    PageResult<ReviewResponseDto> getReviewList(Long productId);
    //리뷰 등록
    void saveReview(Long productId, ReviewSaveRequestDTO reviewSaveRequestDTO);
    //리뷰 수정
    Long updateReview(Long productId, ReviewSaveRequestDTO reviewSaveRequestDTO);
    //리뷰 삭제
    void deleteReview(Long reviewId);
}
