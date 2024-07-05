package com.test.lecture.lecture.controller.dto;

import com.test.lecture.lecture.domain.LectureStatus;
import com.test.lecture.lecture.domain.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class LectureResponse {

    private final long id;
    private final String lectureName;
    private final String teacherName;
    private final LocalDateTime lectureTime;
    private final int totalStudents;
    private final int currentStudents;
    private final LectureStatus lectureStatus;

    public LectureResponse(long id, String lectureName, String teacherName, LocalDateTime lectureTime, int totalStudents, int currentStudents, LectureStatus lectureStatus) {
        this.id = id;
        this.lectureName = lectureName;
        this.teacherName = teacherName;
        this.lectureTime = lectureTime;
        this.totalStudents = totalStudents;
        this.currentStudents = currentStudents;
        this.lectureStatus = lectureStatus;
    }

    public static LectureResponse from(Schedule schedule) {
        return new LectureResponse(
                schedule.getId(),
                schedule.getLecture().getLectureName(),
                schedule.getLecture().getTeacherName(),
                schedule.getTime(),
                schedule.getLecture().getTotalStudents(),
                schedule.getLecture().getCurrentStudents(),
                schedule.getLecture().getLectureStatus()
        );
    }

    public static List<LectureResponse> from(List<Schedule> schedules) {
        return schedules.stream().map(LectureResponse::from).collect(Collectors.toList());
    }
}

