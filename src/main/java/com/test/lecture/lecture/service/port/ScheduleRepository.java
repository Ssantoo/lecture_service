package com.test.lecture.lecture.service.port;

import com.test.lecture.lecture.domain.Lecture;

import java.util.List;

public interface ScheduleRepository {
    List<Lecture> findAllLecturesWithSchedules();
}
