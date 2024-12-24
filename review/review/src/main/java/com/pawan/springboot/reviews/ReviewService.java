package com.pawan.springboot.reviews;

import java.util.List;

public interface ReviewService {
    List<Review> getAllReviews(Long companyId);

    Review createReview(Review review, Long id);

    Review getReview(Long reviewId);

    Review updateReview(Review review, Long reviewId);

    Review deleteReview(Long reviewId);
}
