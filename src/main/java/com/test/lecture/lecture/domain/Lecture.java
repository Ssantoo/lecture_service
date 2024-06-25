package com.test.lecture.lecture.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Lecture {
    private final long id;
    private final String name;
    private final int seat;
    private final int currentSeat;
    private final LocalDateTime lectureTime;
    private final LectureStatus lectureStatus;

    @Builder
    public Lecture(long id, String name, int seat, int currentSeat, LocalDateTime lectureTime, LectureStatus lectureStatus) {
        this.id = id;
        this.name = name;
        this.seat = seat;
        this.currentSeat = currentSeat;
        this.lectureTime = lectureTime;
        this.lectureStatus = lectureStatus;
    }
}
