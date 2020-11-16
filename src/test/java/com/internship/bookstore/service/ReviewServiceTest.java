package com.internship.bookstore.service;

import com.internship.bookstore.api.dto.ReviewResponseDto;
import com.internship.bookstore.model.Review;
import com.internship.bookstore.repository.BookRepository;
import com.internship.bookstore.repository.ReviewRepository;

import com.internship.bookstore.utils.exceptions.RecordNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.internship.TestConstants.ID_ONE;
import static com.internship.TestConstants.TEXT_REVIEW_ONE;
import static com.internship.bookstore.utils.BookTestUtils.BOOK_ONE;
import static com.internship.bookstore.utils.ReviewTestUtils.*;
import static com.internship.bookstore.utils.UserTestUtils.USER_ONE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserService userService;

    @Mock
    private TextValidationService textValidationService;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(reviewService, "messageBookNotFound",
                "Book with id %s was not found");
        ReflectionTestUtils.setField(reviewService, "messageReviewNotFound",
                "Review with id %s was not found");
        ReflectionTestUtils.setField(reviewService, "messageInvalidReview",
                "Invalid review text");
    }

//    @AfterEach
//    void tearDown() {
//        verifyNoMoreInteractions(reviewRepository, bookRepository);
//    }

    @Test
    public void shouldAddReview() {
        final ReviewResponseDto expectedReviewResponseDto = REVIEW_RESPONSE_DTO;

        when(bookRepository.findBookById(ID_ONE)).thenReturn(Optional.of(BOOK_ONE));
        when(textValidationService.validateText(REVIEW_REQUEST_DTO.getTextReview())).thenReturn(true);
        when(reviewRepository.save(any(Review.class))).thenReturn(REVIEW_ONE);
        when(userService.getUser()).thenReturn(USER_ONE);


        final ReviewResponseDto actualReviewResponseDto = reviewService.add(REVIEW_REQUEST_DTO);

        assertAll(
                () -> assertEquals(expectedReviewResponseDto.getReview(), actualReviewResponseDto.getReview()),
                () -> assertEquals(expectedReviewResponseDto.getBook(), actualReviewResponseDto.getBook())
        );

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void shouldThrowRecordNotFoundException() {
        when(bookRepository.findBookById(ID_ONE)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> reviewService.add(REVIEW_REQUEST_DTO));
    }

    @Test
    public void shouldThrowRecordNotFoundExceptionInvalidText() {
        when(bookRepository.findBookById(ID_ONE)).thenReturn(Optional.of(BOOK_ONE));
        when(textValidationService.validateText(REVIEW_REQUEST_DTO.getTextReview())).thenReturn(false);
        assertThrows(RecordNotFoundException.class, () -> reviewService.add(REVIEW_REQUEST_DTO));
    }

    @Test
    public void shouldThrowRecordNotFoundExceptionIT() {
        when(bookRepository.findBookById(ID_ONE)).thenReturn(Optional.of(BOOK_ONE));
        // trying to validate a review text that contains the word "test"
        textValidationService.validateText(TEXT_REVIEW_ONE);
        assertThrows(RecordNotFoundException.class, () -> reviewService.add(REVIEW_REQUEST_DTO));
    }

   @Test
    public void shouldEditReview() {
       final ReviewResponseDto expectedReviewResponseDto = REVIEW_RESPONSE_DTO;

       when(bookRepository.findBookById(ID_ONE)).thenReturn(Optional.of(BOOK_ONE));
       when(textValidationService.validateText(REVIEW_REQUEST_DTO.getTextReview())).thenReturn(true);
       when(reviewRepository.save(any(Review.class))).thenReturn(REVIEW_ONE);
       when(userService.getUser()).thenReturn(USER_ONE);

       final ReviewResponseDto actualReviewResponseDto = reviewService.add(REVIEW_REQUEST_DTO);

       assertEquals(expectedReviewResponseDto.getReview(), actualReviewResponseDto.getReview());

       verify(reviewRepository, times(1)).save(any(Review.class));
   }

   @Test
    public void shouldGetReviewById() {
       final ReviewResponseDto expectedReviewResponseDto = REVIEW_RESPONSE_DTO;

       when(reviewRepository.findReviewById(ID_ONE)).thenReturn(Optional.of(REVIEW_ONE));

       final ReviewResponseDto actualReviewResponseDto = reviewService.getReviewById(ID_ONE);

       assertEquals(expectedReviewResponseDto.getId(), actualReviewResponseDto.getId());

       verify(reviewRepository, times(1)).findReviewById(any(Long.class));
   }

   @Test
    public void shouldDeleteReview() {
       when(reviewRepository.findReviewById(ID_ONE)).thenReturn(Optional.of(REVIEW_ONE));

       reviewService.deleteReviewById(ID_ONE);

       verify(reviewRepository, times(1)).findReviewById(any(Long.class));
       verify(reviewRepository, times(1)).delete(REVIEW_ONE);

   }
}
