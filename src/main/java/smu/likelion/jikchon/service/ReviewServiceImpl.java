package smu.likelion.jikchon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smu.likelion.jikchon.base.PageResult;
import smu.likelion.jikchon.domain.Product;
import smu.likelion.jikchon.domain.Review;
import smu.likelion.jikchon.domain.member.Member;
import smu.likelion.jikchon.dto.review.ReviewResponseDto;
import smu.likelion.jikchon.dto.review.ReviewSaveRequestDTO;
import smu.likelion.jikchon.exception.CustomForbiddenException;
import smu.likelion.jikchon.exception.CustomNotFoundException;
import smu.likelion.jikchon.exception.ErrorCode;
import smu.likelion.jikchon.repository.MemberRepository;
import smu.likelion.jikchon.repository.ProductRepository;
import smu.likelion.jikchon.repository.ReviewRepository;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final LoginService loginService;


    //todo : 리팩토링
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
            throw new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT);
        }
    }

    //todo : 리팩토링
    @Override
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
                new CustomNotFoundException(ErrorCode.NOT_FOUND_REVIEW));

        if (Objects.equals(review.getMember().getId(), loginService.getLoginMemberId())) {
            reviewRepository.delete(review);
        } else {
            throw new CustomForbiddenException(ErrorCode.FORBIDDEN);
        }
    }

    @Override
    @Transactional
    public Long updateReview(Long reviewId, ReviewSaveRequestDTO reviewSaveRequestDTO) {
        Long memberId = loginService.getLoginMemberId();
        Review review = reviewRepository.findReviewsByMemberId(memberId).orElseThrow(() ->
                new CustomNotFoundException(ErrorCode.NOT_FOUND_REVIEW));

        review.setStarRating(reviewSaveRequestDTO.getStarRating());
        review.setContent(reviewSaveRequestDTO.getContent());
        return review.getId();
    }

    //도움 필요 ,,
    @Override
    public PageResult<ReviewResponseDto> getReviewList(Long productId) {
        return PageResult.ok(reviewRepository.findAllByProductId(productId).map(ReviewResponseDto::of));
    }
}