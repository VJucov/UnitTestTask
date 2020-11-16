package com.internship.bookstore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TextValidationService {
    protected boolean validateText(String text)  {
        if (!text.isEmpty() && !text.matches("free|admin|test")) {
            return true;
        }
        log.warn("Invalid text");
        return false;
    }
}
