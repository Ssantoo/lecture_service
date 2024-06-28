package com.test.lecture.lecture.infrastructure.entity;

import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.LectureStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lecture")
public class LectureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lecture_name")
    private String lectureName;

    @Column(name = "teacher_name")
    private String teacherName;

    @Column(name = "total_students")
    private int totalStudents;

    @Column(name = "current_students")
    private int currentStudents;

    @Column(name = "lecture_status")
    @Enumerated(EnumType.STRING)
    private LectureStatus lectureStatus;

    public Lecture toModel() {
        return Lecture.builder()
                .id(id)
                .lectureName(lectureName)
                .teacherName(teacherName)
                .totalStudents(totalStudents)
                .currentStudents(currentStudents)
                .lectureStatus(lectureStatus)
                .build();
    }

    public static LectureEntity from(Lecture lecture) {
        LectureEntity lectureEntity = new LectureEntity();
        lectureEntity.id = lecture.getId();
        lectureEntity.lectureName = lecture.getLectureName();
        lectureEntity.teacherName = lecture.getTeacherName();
        lectureEntity.totalStudents = lecture.getTotalStudents();
        lectureEntity.currentStudents = lecture.getCurrentStudents();
        lectureEntity.lectureStatus = lecture.getLectureStatus();
        return lectureEntity;
    }
}
