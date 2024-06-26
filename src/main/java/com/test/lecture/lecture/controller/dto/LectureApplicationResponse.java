package com.test.lecture.lecture.controller.dto;

import com.test.lecture.lecture.domain.LectureApplication;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class LectureApplicationResponse {
    private final Long lectureId;
    private final String lectureName;
    private final String teacher;
    private final LocalDateTime time;
    private final LocalDate applicationDate;

    public LectureApplicationResponse(Long lectureId, String lectureName, String teacher, LocalDateTime time, LocalDate applicationDate) {
        this.lectureId = lectureId;
        this.lectureName = lectureName;
        this.teacher = teacher;
        this.time = time;
        this.applicationDate = applicationDate;
    }

    public static LectureApplicationResponse from(LectureApplication application) {
        return new LectureApplicationResponse(
                application.getLecture().getId(),
                application.getLecture().getName(),
                application.getLecture().getTeacher(),
                application.getSchedule().getTime(),
                application.getApplicationDate()
        );
    }

    public static List<LectureApplicationResponse> from(List<LectureApplication> applications) {
        return applications.stream().map(LectureApplicationResponse::from).collect(Collectors.toList());
    }
}


