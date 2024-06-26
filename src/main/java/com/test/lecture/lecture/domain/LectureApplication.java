package com.test.lecture.lecture.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
@Getter
public class LectureApplication {
    private final Long id;
    private final User user;
    private final Lecture lecture;
    private final Schedule schedule;
    private final LocalDate applicationDate;

    @Builder
    public LectureApplication(Long id, User user, Lecture lecture, Schedule schedule, LocalDate applicationDate) {
        this.id = id;
        this.user = user;
        this.lecture = lecture;
        this.schedule = schedule;
        this.applicationDate = applicationDate;
    }

}

