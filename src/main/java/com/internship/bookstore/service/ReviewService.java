package com.internship.bookstore.service;


import com.internship.bookstore.api.dto.ReviewRequestDto;
import com.internship.bookstore.api.dto.ReviewResponseDto;
import com.internship.bookstore.model.Book;
import com.internship.bookstore.model.Review;
import com.internship.bookstore.repository.BookRepository;
import com.internship.bookstore.repository.ReviewRepository;
import com.internship.bookstore.utils.exceptions.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.internship.bookstore.utils.mappers.ReviewMapper.mapReviewRequestDtoToReview;
import static com.internship.bookstore.utils.mappers.ReviewMapper.mapReviewToReviewResponseDto;

import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final TextValidationService textValidationService;
    private final UserService userService;
    @Value("${message.book.not-found}")
    private String messageBookNotFound;
    @Value("${message.review.not-found}")
    private String messageReviewNotFound;
    @Value("${message.review.invalid}")
    private String messageInvalidReview;

    @Transactional
    public ReviewResponseDto add(ReviewRequestDto reviewRequestDto) {
        log.info("Adding a review to the book with id [{}]", reviewRequestDto.getBookId());

        Book book = bookRepository.findBookById(reviewRequestDto.getBookId()).orElseThrow(() -> {
            log.warn("Book with id [{}] was not found in the database", reviewRequestDto.getBookId());
            return new RecordNotFoundException(format(messageBookNotFound, reviewRequestDto.getBookId()));
        });

        if (textValidationService.validateText(reviewRequestDto.getTextReview())) {

            Review review = mapReviewRequestDtoToReview.apply(reviewRequestDto);

            review.setBook(book);
            review.setUser(userService.getUser());

            review = reviewRepository.save(review);

            return mapReviewToReviewResponseDto.apply(review);
        }
        log.warn("Invalid review text");
        throw new RecordNotFoundException(messageInvalidReview);
    }

    @Transactional
    public ReviewResponseDto edit(ReviewRequestDto reviewRequestDto) {
        log.info("Updating a review to the book with id [{}]", reviewRequestDto.getBookId());

        Book book = bookRepository.findBookById(reviewRequestDto.getBookId()).orElseThrow(() -> {
            log.warn("Book with id [{}] was not found in the database", reviewRequestDto.getBookId());
            return new RecordNotFoundException(format(messageBookNotFound, reviewRequestDto.getBookId()));
        });

        if (textValidationService.validateText(reviewRequestDto.getTextReview())) {

            Review reviewToBeUpdated = mapReviewRequestDtoToReview.apply(reviewRequestDto);
            reviewToBeUpdated.setBook(book);
            reviewToBeUpdated.setUser(userService.getUser());
            reviewToBeUpdated = reviewRepository.save(reviewToBeUpdated);

            return mapReviewToReviewResponseDto.apply(reviewToBeUpdated);
        }
        log.warn("Invalid review text");
        throw new RecordNotFoundException(messageInvalidReview);
    }

    public ReviewResponseDto getReviewById(Long id) {
        Review review = reviewRepository.findReviewById(id).orElseThrow(
                () -> { log.warn("Review with id [{}] was not found in database", id);
                    return new RecordNotFoundException(
                            format(messageReviewNotFound, id));
                });

        return mapReviewToReviewResponseDto.apply(review);
    }

    @Transactional
    public void deleteReviewById(Long id) {
        log.warn("Deleting review with id [{}]", id);

        Review review = reviewRepository.findReviewById(id).orElseThrow(
                () -> { log.warn("Review with id [{}] was not found in database", id);
                    return new RecordNotFoundException(
                            format(messageReviewNotFound, id));
                });

        reviewRepository.delete(review);
    }

}
