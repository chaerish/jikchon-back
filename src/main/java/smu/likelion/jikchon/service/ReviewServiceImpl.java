package smu.likelion.jikchon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.domain.Review;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.dto.review.ReviewSaveRequestDTO;
import smu.likelion.jikchon.exception.CustomNotFoundException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.repository.MemberRepository;
import smu.likelion.jikchon.repository.ProductRepository;
import smu.likelion.jikchon.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final LoginService loginService;

    @Override
    public void saveReview(Long productId, ReviewSaveRequestDTO reviewSaveRequestDTO) {
        Long memberId = loginService.getLoginMemberId();
        Product product = productRepository.findById(productId).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT);
        });
        Member member = memberRepository.findById(memberId).orElseThrow(() -> {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER);
        });
        if (productRepository.findById(productId).isPresent() && memberRepository.findById(memberId).isPresent()) {
            reviewRepository.save(Review.builder()
                    .content(reviewSaveRequestDTO.getContent())
                    .starRating(reviewSaveRequestDTO.getStarRating()).build());
        } else {
            throw new Exception(ErrorCode.NOT_PURCHASE_PRODUCT);
        }
    }

    @Override
    public int deleteReview(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        Optional<Member> member = loginService.getMemberId();
        if (review.get() == null) {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_REVIEW);
        }
        if (member.get() == null) {
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER);
        } else {
            reviewRepository.delete(review.get());
            return 200;
        }
    }

    @Override
    public Long updateReview(Long reviewId, ReviewSaveRequestDTO reviewSaveRequestDTO) {
        Long memberId = loginService.getMemberId();
        Optional<Review> review = reviewRepository.findReviewsByMemberId(memberId);
        if (review.isPresent()) {
            Review review1 = review.get();
            review1.setStarRating(reviewSaveRequestDTO.getStarRating());
            review1.setContent(reviewSaveRequestDTO.getContent());
            reviewRepository.save(review1);
            return review1.getId();

        } else {
            throw new Exception(ErrorCode.REVIEW_NOT_FOUND);
        }

    }

    //도움 필요 ,,
    @Override
    public List<Review> getReviewList(Long productId) {
        List<Review> reviews = reviewRepository.findAllByProductId(productId);
        return reviews;
    }
}
