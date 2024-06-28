package com.test.lecture.lecture.infrastructure;

import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.Schedule;
import com.test.lecture.lecture.domain.User;
import com.test.lecture.lecture.infrastructure.entity.UserEntity;
import com.test.lecture.lecture.service.port.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.test.lecture.lecture.infrastructure.entity.ScheduleEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final ScheduleJpaRepository scheduleJpaRepository;
    @Override
    public List<Schedule> findAll() {
        return scheduleJpaRepository.findAll().stream().map(ScheduleEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Schedule> findById(Long scheduleId) {
        return scheduleJpaRepository.findById(scheduleId).map(ScheduleEntity::toModel);
    }

    @Override
    public void save(Schedule schedule) {
        scheduleJpaRepository.save(ScheduleEntity.from(schedule));
    }

    @Override
    public Optional<Schedule> findByIdWithLock(Long scheduleId) {
        return scheduleJpaRepository.findByIdWithLock(scheduleId).map(ScheduleEntity::toModel);
    }


}
