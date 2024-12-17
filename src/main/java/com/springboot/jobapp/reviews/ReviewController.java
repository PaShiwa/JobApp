package com.springboot.jobapp.reviews;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies/{companyId}")
public class ReviewController {
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/reviews")
    private ResponseEntity<?> getAllReviewsForCompany(@PathVariable Long companyId) {
        List<Review> reviews = reviewService.getAllReviews(companyId);
        if (reviews.size() >= 1) {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }
        return new ResponseEntity<>("No reviews found!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/reviews/{reviewId}")
    private ResponseEntity<?> getReview(@PathVariable Long companyId, @PathVariable Long reviewId) {
        Review review = reviewService.getReview(companyId, reviewId);
        if (review != null) {
            return new ResponseEntity<>(review, HttpStatus.OK);
        }
        return new ResponseEntity<>("No review found", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/reviews")
    private ResponseEntity<?> addReview(@RequestBody Review review, @PathVariable Long companyId) {
        Review savedReview = reviewService.createReview(review, companyId);
        if (savedReview != null) {
            return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Can't add a review. Add Company first!", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/reviews/{reviewId}")
    private ResponseEntity<?> updateReview(@RequestBody Review review, @PathVariable Long companyId, @PathVariable Long reviewId) {
        Review updatedReview = reviewService.updateReview(review, companyId, reviewId);
        if (updatedReview != null) {
            return new ResponseEntity<>(updatedReview, HttpStatus.OK);
        }
        return new ResponseEntity<>("Review does not exist!. Add a review?", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/reviews/{reviewId}")
    private ResponseEntity<?> deleteReview(@PathVariable Long companyId, @PathVariable Long reviewId) {
        Review deletedReview = reviewService.deleteReview(companyId, reviewId);
        if (deletedReview != null) {
            return new ResponseEntity<>(deletedReview, HttpStatus.OK);
        }
        return new ResponseEntity<>("Can't delete. Review does not exist!", HttpStatus.NOT_FOUND);
    }

}
