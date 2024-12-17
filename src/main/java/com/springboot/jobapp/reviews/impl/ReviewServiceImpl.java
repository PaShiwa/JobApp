package com.springboot.jobapp.reviews.impl;

import com.springboot.jobapp.company.Company;
import com.springboot.jobapp.company.CompanyService;
import com.springboot.jobapp.reviews.Review;
import com.springboot.jobapp.reviews.ReviewRepository;
import com.springboot.jobapp.reviews.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CompanyService companyService;

    public ReviewServiceImpl(ReviewRepository reviewRepository, CompanyService companyService) {
        this.reviewRepository = reviewRepository;
        this.companyService = companyService;
    }

    @Override
    public List<Review> getAllReviews(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public Review getReview(Long companyId, Long reviewId) {
        List<Review> reviews = reviewRepository.findByCompanyId(companyId);
        return reviews.stream()
                .filter(review -> review.getId().equals(reviewId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Review createReview(Review review, Long id) {
        Company company = companyService.getCompanyById(id);
        if (company != null) {
            review.setCompany(company);
            return reviewRepository.save(review);
        }
        return null;
    }

    @Override
    public Review updateReview(Review updatedReview, Long companyId, Long reviewId) {
        Company company = companyService.getCompanyById(companyId);
        if (company == null) {
            throw new IllegalArgumentException("Company with ID " + companyId + " does not exist.");
        }

        List<Review> companyReviews = company.getReviews();
        Review existingReview = companyReviews.stream()
                .filter(review -> review.getId().equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Review with ID " + reviewId + " does not exist for Company with ID " + companyId));

        existingReview.setTitle(updatedReview.getTitle());
        existingReview.setDescription(updatedReview.getDescription());
        existingReview.setRating(updatedReview.getRating());

        return reviewRepository.save(existingReview);
    }


    @Override
    public Review deleteReview(Long companyId, Long reviewId) {
        Company company = companyService.getCompanyById(companyId);
        if (company == null) {
            throw new IllegalArgumentException("Company with ID " + companyId + " does not exist.");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review with ID " + reviewId + " does not exist."));


        // Ensure the review belongs to the company
        if (!review.getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException("Review with ID " + reviewId + " does not belong to Company with ID " + companyId);
        }
        reviewRepository.delete(review);
        return review;
    }

}
