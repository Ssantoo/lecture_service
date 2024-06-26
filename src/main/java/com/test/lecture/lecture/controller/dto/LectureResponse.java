package com.test.lecture.lecture.controller.dto;

import com.test.lecture.lecture.domain.Lecture;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class LectureResponse {

    private final Long id;
    private final String name;
    private final String teacher;
    private final int maxCapacity;
    private final List<ScheduleResponse> schedules;

    public LectureResponse(Long id, String name, String teacher, int maxCapacity, List<ScheduleResponse> schedules) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.maxCapacity = maxCapacity;
        this.schedules = schedules;
    }

    public static LectureResponse from(Lecture lecture) {
        return new LectureResponse(
                lecture.getId(),
                lecture.getName(),
                lecture.getTeacher(),
                lecture.getMaxCapacity(),
                ScheduleResponse.from(lecture.getSchedules())
        );
    }

    public static List<LectureResponse> from(List<Lecture> lectures) {
        return lectures.stream().map(LectureResponse::from).collect(Collectors.toList());
    }
}



