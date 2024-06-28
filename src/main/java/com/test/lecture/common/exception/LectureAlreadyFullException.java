package com.test.lecture.common.exception;

public class LectureAlreadyFullException extends RuntimeException {
    public LectureAlreadyFullException(String message) {
        super(message);
    }
}
