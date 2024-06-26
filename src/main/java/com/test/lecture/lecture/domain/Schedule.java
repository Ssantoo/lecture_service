package com.test.lecture.lecture.domain;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class Schedule {
    private final Long id;
    private final LocalDateTime time;
    private final Lecture lecture;

    @Builder
    public Schedule(Long id, LocalDateTime time, Lecture lecture) {
        this.id = id;
        this.time = time;
        this.lecture = lecture;
    }


}

