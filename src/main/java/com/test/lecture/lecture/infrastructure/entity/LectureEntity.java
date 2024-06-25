package com.test.lecture.lecture.infrastructure.entity;

import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.LectureStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lecture")
public class LectureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "lecture_time")
    private LocalDateTime lectureTime;

    @Column(name = "seat")
    private int seat;

    @Column(name = "current_seat")
    private int currentSeat;

    @Column
    @Enumerated(EnumType.STRING)
    private LectureStatus lectureStatus;

    public static LectureEntity from(Lecture lecture) {
        LectureEntity lectureEntity = new LectureEntity();
        lectureEntity.id = lecture.getId();
        lectureEntity.name = lecture.getName();
        lectureEntity.lectureTime = lecture.getLectureTime();
        lectureEntity.seat = lecture.getSeat();
        lectureEntity.currentSeat = lecture.getCurrentSeat();
        lectureEntity.lectureStatus = lecture.getLectureStatus();
        return lectureEntity;
    }

    public Lecture toModel() {
        return new Lecture(
                id,
                name,
                seat,
                currentSeat,
                lectureTime,
                lectureStatus
        );
    }
}

