package com.springboot.jobapp.reviews;

import java.util.List;

public interface ReviewService {
    List<Review> getAllReviews(Long companyId);

    Review createReview(Review review, Long id);

    Review getReview(Long companyId, Long reviewId);

    Review updateReview(Review review, Long companyId, Long reviewId);

    Review deleteReview(Long companyId, Long reviewId);
}
