package com.example.holidayapp.exception;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        HashMap<String, String> map = new HashMap<>();
        map.put("message", ex.getMessage());
        map.put("error", "Internal Server Error");
        map.put("timestamp", String.valueOf(LocalDateTime.now()));
        return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(HolidayApiException.class)
    public ResponseEntity<Map<String, String>> handleApiExceptions(HolidayApiException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        errors.put("error", "Holiday API Error");
        errors.put("timestamp", String.valueOf(LocalDateTime.now()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_GATEWAY);
    }


}
