package com.test.lecture.lecture.infrastructure.entity;

import com.test.lecture.lecture.domain.LectureApplication;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "lecture_applications")
public class LectureApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "lecture_id")
    private LectureEntity lecture;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private ScheduleEntity schedule;

    private LocalDate applicationDate;

    public static LectureApplicationEntity from(LectureApplication application) {
        LectureApplicationEntity entity = new LectureApplicationEntity();
        entity.id = application.getId();
        entity.user = UserEntity.from(application.getUser());
        entity.lecture = LectureEntity.from(application.getLecture());
        entity.schedule = ScheduleEntity.from(application.getSchedule());
        entity.applicationDate = application.getApplicationDate();
        return entity;
    }

    public LectureApplication toModel() {
        return LectureApplication.builder()
                .id(id)
                .user(user.toModel())
                .lecture(lecture.toModel())
                .schedule(schedule.toModel())
                .applicationDate(applicationDate)
                .build();
    }
}


