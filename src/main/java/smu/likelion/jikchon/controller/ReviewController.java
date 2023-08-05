package smu.likelion.jikchon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import smu.likelion.jikchon.base.BaseResponse;
import smu.likelion.jikchon.dto.review.ReviewSaveRequestDTO;
import smu.likelion.jikchon.service.ReviewService;

import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("/reviews/{productId}")
    public BaseResponse<List> getReviewList(@PathVariable Long productId){
        return BaseResponse.ok(reviewService.getReviewList(productId));
    }
    @DeleteMapping("/{reviewsId}")
    public BaseResponse<Long> deleteReview(@PathVariable Long reviewId){
        return BaseResponse.ok(reviewService.deleteReview(reviewId));
    }
    @PutMapping("/{reviewsId}")
    public BaseResponse<Long> updateReview(@RequestBody Long productId, @RequestBody ReviewSaveRequestDTO reviewSaveRequestDTO) {
        return BaseResponse.ok(reviewService.updateReview(productId, reviewSaveRequestDTO));
    }
}
