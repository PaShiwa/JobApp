package com.pawan.springboot.reviews.impl;

import com.pawan.springboot.reviews.Review;
import com.pawan.springboot.reviews.ReviewRepository;
import com.pawan.springboot.reviews.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public Review getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        return review;
    }

    @Override
    public Review createReview(Review review, Long companyId) {
        if (companyId != null && review != null) {
            review.setCompanyId(companyId);
            return reviewRepository.save(review);
        }
        return null;
    }

    @Override
    public Review updateReview(Review updatedReview, Long reviewId) {
        Review existingReview = reviewRepository.findById(reviewId).orElse(null);
        if (existingReview != null) {
            existingReview.setTitle(updatedReview.getTitle());
            existingReview.setDescription(updatedReview.getDescription());
            existingReview.setRating(updatedReview.getRating());
            return reviewRepository.save(existingReview);
        }
        return null;
    }


    @Override
    public Review deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review with ID " + reviewId + " does not exist."));
        if (review != null) {
            reviewRepository.delete(review);
            return review;
        }
        return null;
    }
}
