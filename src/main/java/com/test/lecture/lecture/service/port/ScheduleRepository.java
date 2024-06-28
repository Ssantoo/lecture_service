package com.test.lecture.lecture.service.port;

import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {
    List<Schedule> findAll();

    Optional<Schedule> findById(Long scheduleId);

    void save(Schedule schedule);

    Optional<Schedule> findByIdWithLock(Long scheduleId);
}
