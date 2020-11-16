package com.internship.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.bookstore.api.controller.ReviewRestController;
import com.internship.bookstore.service.ReviewService;
import com.internship.bookstore.service.UserService;
import com.internship.it.controller.BaseController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.internship.TestConstants.ID_ONE;
import static com.internship.bookstore.utils.ReviewTestUtils.REVIEW_REQUEST_DTO;
import static com.internship.bookstore.utils.ReviewTestUtils.REVIEW_RESPONSE_DTO;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReviewRestController.class)
public class ReviewRestControllerTest extends BaseController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void shouldAddReview() throws Exception {
        when(reviewService.add(REVIEW_REQUEST_DTO)).thenReturn(REVIEW_RESPONSE_DTO);

        mockMvc.perform(post("/review/add")
                .content(objectMapper.writeValueAsString(REVIEW_REQUEST_DTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(createExpectedBody(REVIEW_RESPONSE_DTO)));

        verify(reviewService).add(REVIEW_REQUEST_DTO);
    }

    @Test
    @WithMockUser
    public void shouldEditReview() throws Exception {
        when(reviewService.edit(REVIEW_REQUEST_DTO)).thenReturn(REVIEW_RESPONSE_DTO);

        mockMvc.perform(put("/review/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(REVIEW_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(createExpectedBody(REVIEW_RESPONSE_DTO)));

        verify(reviewService).edit(REVIEW_REQUEST_DTO);
    }

    @Test
    @WithMockUser
    public void shouldGetReviewById() throws Exception {
        when(reviewService.getReviewById(ID_ONE)).thenReturn(REVIEW_RESPONSE_DTO);

        mockMvc.perform(get("/review/{id}",ID_ONE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(createExpectedBody(REVIEW_RESPONSE_DTO)));

        verify(reviewService).getReviewById(ID_ONE);
    }

    @Test
    @WithMockUser
    public void shouldDeleteReviewById() throws Exception {

        mockMvc.perform(delete("/review/delete/{id}", ID_ONE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createExpectedBody(REVIEW_REQUEST_DTO)))
                .andExpect(status().isOk());

        verify(reviewService).deleteReviewById(ID_ONE);
    }
}
