package com.test.lecture.lecture.controller.response;

import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.LectureStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class LectureResponse {

    private final long id;
    private final String name;
    private final LocalDateTime lectureTime;
    private final int seat;
    private final int currentSeat;
    private final LectureStatus lectureStatus;

    public LectureResponse(long id, String name, LocalDateTime lectureTime, int seat, int currentSeat, LectureStatus lectureStatus) {
        this.id = id;
        this.name = name;
        this.lectureTime = lectureTime;
        this.seat = seat;
        this.currentSeat = currentSeat;
        this.lectureStatus = lectureStatus;
    }

    public static LectureResponse from(Lecture lecture) {
        return new LectureResponse(
                lecture.getId(),
                lecture.getName(),
                lecture.getLectureTime(),
                lecture.getSeat(),
                lecture.getCurrentSeat(),
                lecture.getLectureStatus()
        );
    }

    public static List<LectureResponse> from(List<Lecture> lectures) {
        return lectures.stream().map(LectureResponse::from).collect(Collectors.toList());
    }
}

