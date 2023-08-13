package smu.likelion.jikchon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import smu.likelion.jikchon.base.BaseResponse;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.dto.review.ReviewResponseDto;
import smu.likelion.jikchon.dto.review.ReviewSaveRequestDto;
import smu.likelion.jikchon.service.ReviewService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("/reviews/{productId}")
    public BaseResponse<PageResult<ReviewResponseDto>> getReviewList(@PathVariable Long productId, Pageable pageable){
        return BaseResponse.ok(reviewService.getReviewList(productId, pageable));
    }
    @DeleteMapping("/{reviewId}")
    public BaseResponse<Void> deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
        return BaseResponse.ok();
    }
    @PutMapping("/{reviewsId}")
    public BaseResponse<Long> updateReview(@RequestBody Long productId, @RequestBody ReviewSaveRequestDto reviewSaveRequestDTO) {
        return BaseResponse.ok(reviewService.updateReview(productId, reviewSaveRequestDTO));
    }
}
