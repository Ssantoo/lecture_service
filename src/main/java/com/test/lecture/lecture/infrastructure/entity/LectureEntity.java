package com.test.lecture.lecture.infrastructure.entity;

import com.test.lecture.lecture.domain.Lecture;
import jakarta.persistence.*;

@Entity
@Table(name = "lectures")
public class LectureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String teacher;
    private int maxCapacity;

    public static LectureEntity from(Lecture lecture) {
        LectureEntity lectureEntity = new LectureEntity();
        lectureEntity.id = lecture.getId();
        lectureEntity.name = lecture.getName();
        lectureEntity.teacher = lecture.getTeacher();
        lectureEntity.maxCapacity = lecture.getMaxCapacity();
        return lectureEntity;
    }

    public Lecture toModel() {
        return Lecture.builder()
                .id(id)
                .name(name)
                .teacher(teacher)
                .maxCapacity(maxCapacity)
                .build();
    }
}




