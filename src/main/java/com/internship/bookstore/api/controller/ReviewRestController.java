package com.internship.bookstore.api.controller;

import com.internship.bookstore.api.dto.ReviewRequestDto;
import com.internship.bookstore.api.dto.ReviewResponseDto;
import com.internship.bookstore.api.exchange.Response;
import com.internship.bookstore.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Objects;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewRestController {
    private final ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<Response<ReviewResponseDto>> addReview(
            @RequestBody @Valid ReviewRequestDto reviewRequestDto,
            Errors validationErrors) {

        if (validationErrors.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(validationErrors.getFieldError()).getDefaultMessage());
        }
        return ok(Response.build(reviewService.add(reviewRequestDto)));
    }

    @PutMapping("/edit")
    public ResponseEntity<Response<ReviewResponseDto>> editReview(
            @RequestBody @Valid ReviewRequestDto reviewRequestDto,
            Errors validationErrors) {

        if (validationErrors.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(validationErrors.getFieldError()).getDefaultMessage());
        }
        return ok(Response.build(reviewService.edit(reviewRequestDto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ReviewResponseDto>> getReviewById(@PathVariable Long id) {
        return ok(Response.build(reviewService.getReviewById(id)));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReviewById(id);
    }

}
