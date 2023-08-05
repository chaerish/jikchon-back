package smu.likelion.jikchon.service;

import smu.likelion.jikchon.domain.Review;
import smu.likelion.jikchon.dto.review.ReviewSaveRequestDTO;

import java.util.List;

public interface ReviewService {
    //리뷰 전체 조회
    List<Review> getReviewList(Long productId);
    //리뷰 등록
    void saveReview(Long productId, ReviewSaveRequestDTO reviewSaveRequestDTO);
    //리뷰 수정
    Long updateReview(Long productId, ReviewSaveRequestDTO reviewSaveRequestDTO);
    //리뷰 삭제
    Long deleteReview(Long reviewId);
}
