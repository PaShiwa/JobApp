package com.pawan.springboot.reviews;

import com.pawan.springboot.reviews.messaging.ReviewMessageProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private ReviewService reviewService;
    private ReviewMessageProducer reviewMessageProducer;

    public ReviewController(ReviewService reviewService, ReviewMessageProducer reviewMessageProducer) {
        this.reviewService = reviewService;
        this.reviewMessageProducer=reviewMessageProducer;
    }

    @GetMapping()
    private ResponseEntity<?> getAllReviewsForCompany(@RequestParam Long companyId) {
        List<Review> reviews = reviewService.getAllReviews(companyId);
        if (reviews.size() >= 1) {
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }
        return new ResponseEntity<>("No reviews found!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{reviewId}")
    private ResponseEntity<?> getReviewPerId(@PathVariable Long reviewId) {
        Review review = reviewService.getReview(reviewId);
        if (review != null) {
            return new ResponseEntity<>(review, HttpStatus.OK);
        }
        return new ResponseEntity<>("No review found", HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    private ResponseEntity<?> addReview(@RequestBody Review review, @RequestParam Long companyId) {
        Review savedReview = reviewService.createReview(review, companyId);
        if (savedReview != null) {
            reviewMessageProducer.sendMessage(review);
            return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Can't add a review. Add Company first!", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("{reviewId}")
    private ResponseEntity<?> updateReview(@RequestBody Review review, @PathVariable Long reviewId) {
        Review updatedReview = reviewService.updateReview(review, reviewId);
        if (updatedReview != null) {
            return new ResponseEntity<>(updatedReview, HttpStatus.OK);
        }
        return new ResponseEntity<>("Review does not exist!. Add a review?", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{reviewId}")
    private ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        Review deletedReview = reviewService.deleteReview(reviewId);
        if (deletedReview != null) {
            return new ResponseEntity<>(deletedReview, HttpStatus.OK);
        }
        return new ResponseEntity<>("Can't delete. Review does not exist!", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/averageRating")
    public double getAverageRating(@RequestParam Long companyId){
        List<Review> reviewList = reviewService.getAllReviews(companyId);
        return reviewList.stream().mapToDouble(Review::getRating).average()
                .orElse(0.0);
    }
}
