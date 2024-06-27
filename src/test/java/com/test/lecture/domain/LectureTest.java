package com.test.lecture.domain;

import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.LectureStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LectureTest {

    @Test
    void 강의가_가득_차지_않았을_때_isFull_확인() {
        // given
        Lecture lecture = Lecture.builder()
                .id(1L)
                .lectureName("스프링")
                .teacherName("김선생님")
                .totalStudents(30)
                .currentStudents(25)
                .lectureStatus(LectureStatus.OPEN)
                .build();

        // when
        boolean isFull = lecture.isFull();

        // then
        assertFalse(isFull);
    }

    @Test
    void 강의가_가득_찼을_때_isFull_확인() {
        // given
        Lecture lecture = Lecture.builder()
                .id(1L)
                .lectureName("스프링")
                .teacherName("김선생님")
                .totalStudents(30)
                .currentStudents(30)
                .lectureStatus(LectureStatus.OPEN)
                .build();

        // when
        boolean isFull = lecture.isFull();

        // then
        assertTrue(isFull);
    }

    @Test
    void 강의에_학생_추가() {
        // given
        Lecture lecture = Lecture.builder()
                .id(1L)
                .lectureName("스프링")
                .teacherName("김선생님")
                .totalStudents(30)
                .currentStudents(25)
                .lectureStatus(LectureStatus.OPEN)
                .build();

        // when
        lecture.incrementCurrentStudents();

        // then
        assertEquals(26, lecture.getCurrentStudents());
    }

    @Test
    void 가득_찬_강의에_학생_추가_불가() {
        // given
        Lecture lecture = Lecture.builder()
                .id(1L)
                .lectureName("스프링")
                .teacherName("김선생님")
                .totalStudents(30)
                .currentStudents(30)
                .lectureStatus(LectureStatus.OPEN)
                .build();

        // when
        lecture.incrementCurrentStudents();

        // then
        assertEquals(30, lecture.getCurrentStudents());
    }

}
