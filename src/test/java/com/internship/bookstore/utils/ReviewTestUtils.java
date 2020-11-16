package com.internship.bookstore.utils;

import com.internship.bookstore.api.dto.ReviewRequestDto;
import com.internship.bookstore.api.dto.ReviewResponseDto;
import com.internship.bookstore.model.Review;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.internship.TestConstants.*;
import static com.internship.bookstore.utils.BookTestUtils.BOOK_ONE;
import static com.internship.bookstore.utils.UserTestUtils.USER_ONE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewTestUtils {

    public static final Review REVIEW_ONE = Review.builder()
            .id(ID_ONE)
            .book(BOOK_ONE)
            .user(USER_ONE)
            .textReview(TEXT_REVIEW)
            .build();

    public static final ReviewRequestDto REVIEW_REQUEST_DTO = ReviewRequestDto.builder()
            .bookId(ID_ONE)
            .textReview(TEXT_REVIEW)
            .build();

    public static final ReviewResponseDto REVIEW_RESPONSE_DTO = ReviewResponseDto.builder()
            .id(ID_ONE)
            .book(BOOK_TITLE_ONE)
            .review(TEXT_REVIEW)
            .build();

}
