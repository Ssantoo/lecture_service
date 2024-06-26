package com.test.lecture.common.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.lecture.common.domain.exception.LectureException;
import com.test.lecture.common.domain.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorResponse resourceNotFoundException(ResourceNotFoundException exception) {
        return new ErrorResponse(NOT_FOUND, exception.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LectureException.class)
    public ErrorResponse lectureApplicationException(LectureException exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse genericException(Exception exception) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "에러가 발생했습니다.");
    }

    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorResponse {
        @JsonProperty("status")
        private HttpStatus status;
        @JsonProperty("message")
        private String message;
    }
}
