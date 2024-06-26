package com.test.lecture.lecture.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class Lecture {
    private final Long id;
    private final String name;
    private final String teacher;
    private final int maxCapacity;
    private final List<Schedule> schedules;

    @Builder
    public Lecture(Long id, String name, String teacher, int maxCapacity, List<Schedule> schedules) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.maxCapacity = maxCapacity;
        this.schedules = schedules;
    }


}

