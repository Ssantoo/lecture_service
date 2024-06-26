package com.test.lecture.lecture.infrastructure.entity;

import com.test.lecture.lecture.domain.Schedule;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedules")
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private LectureEntity lecture;

    public static ScheduleEntity from(Schedule schedule) {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.id = schedule.getId();
        scheduleEntity.time = schedule.getTime();
        scheduleEntity.lecture = LectureEntity.from(schedule.getLecture());
        return scheduleEntity;
    }

    public Schedule toModel() {
        return Schedule.builder()
                .id(id)
                .time(time)
                .lecture(lecture.toModel())
                .build();
    }

}


