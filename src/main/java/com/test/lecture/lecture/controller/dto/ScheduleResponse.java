package com.test.lecture.lecture.controller.dto;

import com.test.lecture.lecture.domain.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ScheduleResponse {
    private final Long id;
    private final LocalDateTime time;

    public ScheduleResponse(Long id, LocalDateTime time) {
        this.id = id;
        this.time = time;
    }

    public static ScheduleResponse from(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getTime()
        );
    }

    public static List<ScheduleResponse> from(List<Schedule> schedules) {
        return schedules.stream().map(ScheduleResponse::from).collect(Collectors.toList());
    }
}


