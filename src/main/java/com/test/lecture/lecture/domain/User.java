package com.test.lecture.lecture.domain;


import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
    private final Long id;
    private final String name;

    @Builder
    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
