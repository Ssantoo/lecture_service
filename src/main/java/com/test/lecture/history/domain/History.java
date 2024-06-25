package com.test.lecture.history.domain;

import com.test.lecture.lecture.domain.Lecture;
import com.test.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class History {
    private final long id;
    private final Lecture lecture;
    private final User user;
    private final LocalDateTime appliedTime;

    @Builder
    public History(long id, Lecture lecture, User user, LocalDateTime appliedTime) {
        this.id = id;
        this.lecture = lecture;
        this.user = user;
        this.appliedTime = appliedTime;
    }
}
