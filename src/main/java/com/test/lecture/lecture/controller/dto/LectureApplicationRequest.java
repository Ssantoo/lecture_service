package com.test.lecture.lecture.controller.dto;

import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.LectureApplication;
import com.test.lecture.lecture.domain.Schedule;
import com.test.lecture.lecture.domain.User;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class LectureApplicationRequest {
    private Long userId;
    private Long lectureId;
    private Long scheduleId;

    public LectureApplication toDomain() {
        return LectureApplication.builder()
                .user(User.builder().id(userId).build())
                .lecture(Lecture.builder().id(lectureId).build())
                .schedule(Schedule.builder().id(scheduleId).build())
                .applicationDate(LocalDate.now())
                .build();
    }
}


