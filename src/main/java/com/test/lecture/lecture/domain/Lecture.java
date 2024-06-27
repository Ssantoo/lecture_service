package com.test.lecture.lecture.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Lecture {
    private final Long id;
    private final String lectureName;
    private final String teacherName;
    private final int totalStudents;
    private int currentStudents;
    private final LectureStatus lectureStatus;

    @Builder
    public Lecture(Long id, String lectureName, String teacherName,int totalStudents, int currentStudents, LectureStatus lectureStatus) {
        this.id = id;
        this.lectureName = lectureName;
        this.teacherName = teacherName;
        this.totalStudents = totalStudents;
        this.currentStudents = currentStudents;
        this.lectureStatus = lectureStatus;
    }

    public boolean isFull() {
       return currentStudents >= totalStudents;
    }

    public void incrementCurrentStudents() {
        if (!isFull()) currentStudents++;
    }



}

