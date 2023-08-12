package smu.likelion.jikchon.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import smu.likelion.jikchon.base.BaseResponse;
import smu.likelion.jikchon.dto.review.ReviewSaveRequestDTO;
import smu.likelion.jikchon.service.ReviewService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("/reviews/{productId}")
    public BaseResponse<List> getReviewList(@PathVariable Long productId){
        return BaseResponse.ok(reviewService.getReviewList(productId));
    }
    @DeleteMapping("/{reviewId}")
    public BaseResponse<Void> deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
        return BaseResponse.ok();
    }
    @PutMapping("/{reviewsId}")
    public BaseResponse<Long> updateReview(@RequestBody Long productId, @RequestBody ReviewSaveRequestDTO reviewSaveRequestDTO) {
        return BaseResponse.ok(reviewService.updateReview(productId, reviewSaveRequestDTO));
    }
}
