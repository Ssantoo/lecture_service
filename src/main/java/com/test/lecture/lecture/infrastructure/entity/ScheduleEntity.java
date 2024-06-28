package com.test.lecture.lecture.infrastructure.entity;

import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.Schedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedule")
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private LectureEntity lecture;

    @Column(name = "time")
    private LocalDateTime time;


    public Schedule toModel() {
        return Schedule.builder()
                .id(id)
                .time(time)
                .lecture(lecture.toModel())
                .build();
    }

    public static ScheduleEntity from(Schedule schedule) {
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.id = schedule.getId();
        scheduleEntity.time = schedule.getTime();
        scheduleEntity.lecture = LectureEntity.from(schedule.getLecture());
        return scheduleEntity;
    }

}
