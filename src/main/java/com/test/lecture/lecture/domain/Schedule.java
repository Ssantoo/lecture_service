package com.test.lecture.lecture.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
//@EqualsAndHashCode(of = {"lecture", "time"})
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

    public boolean isSameSchedule(Schedule other) {
        return this.lecture.isSameLecture(other.lecture) && this.time.getDayOfWeek().equals(other.time.getDayOfWeek());
    }
}
