package com.test.lecture.common.exception;

public class AlreadyApplyException  extends RuntimeException{
    public AlreadyApplyException(String message) {
        super(message);
    }
}
