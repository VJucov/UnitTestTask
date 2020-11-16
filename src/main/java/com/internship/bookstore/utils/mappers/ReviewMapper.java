package com.internship.bookstore.utils.mappers;

import com.internship.bookstore.api.dto.ReviewRequestDto;
import com.internship.bookstore.api.dto.ReviewResponseDto;
import com.internship.bookstore.model.Review;

import java.util.function.Function;

public class ReviewMapper {
    public static final Function<Review, ReviewResponseDto> mapReviewToReviewResponseDto =
            review -> ReviewResponseDto.builder()
                .id(review.getId())
                .book(review.getBook().getTitle())
                .review(review.getTextReview())
                .build();
    public static final Function<ReviewRequestDto, Review> mapReviewRequestDtoToReview =
            reviewRequestDto -> Review.builder()
                .textReview(reviewRequestDto.getTextReview())
                .build();
}
