package com.adp.coinchange.exceptionhandler;

import com.adp.coinchange.dto.ErrorResponse;
import com.adp.coinchange.exception.InSufficientCoinsException;
import com.adp.coinchange.exception.InValidBillException;
import com.adp.coinchange.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String MESSAGE_REQUIRED = "Exception message is required";

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final HttpServletRequest request,
                                                         final Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapErrorResponse(request.getRequestURI(), request.getMethod(), exception.getMessage()));
    }

    @ExceptionHandler(value = InValidBillException.class)
    public ResponseEntity<ErrorResponse> handleInValidBillException(final HttpServletRequest request,
                                                                    final InValidBillException inValidBillException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(mapErrorResponse(request.getRequestURI(), request.getMethod(), inValidBillException.getMessage()));
    }

    @ExceptionHandler(value = InSufficientCoinsException.class)
    public ResponseEntity<ErrorResponse> handleInSufficientCoinsException(final HttpServletRequest request,
                                                                          final InSufficientCoinsException inSufficientCoinsException) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(mapErrorResponse(request.getRequestURI(), request.getMethod(), inSufficientCoinsException.getMessage()));
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(final HttpServletRequest request,
                                                                         final ResourceNotFoundException resourceNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapErrorResponse(request.getRequestURI(), request.getMethod(), resourceNotFoundException.getMessage()));
    }

    private ErrorResponse mapErrorResponse(final String uri, final String method, final String message) {
        Assert.notEmpty(Collections.singleton(message), () -> MESSAGE_REQUIRED);
        return new ErrorResponse(uri, method, message);
    }
}
