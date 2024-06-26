package com.test.lecture.lecture.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.Schedule;
import com.test.lecture.lecture.infrastructure.entity.LectureEntity;
import com.test.lecture.lecture.infrastructure.entity.ScheduleEntity;
import com.test.lecture.lecture.service.port.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl  implements ScheduleRepository {

    private ScheduleJpaRepository scheduleJpaRepository;

    @Override
    public List<Lecture> findAllLecturesWithSchedules() {
        return null;
    }
}
