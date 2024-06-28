package com.test.lecture.domain;

import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.LectureStatus;
import com.test.lecture.lecture.domain.Schedule;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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

    @Test
    void 같은_강의_비교() {
        // Given
        Lecture lecture1 = new Lecture(1L, "스프링", "김선생님", 30, 29, LectureStatus.OPEN);
        Lecture lecture2 = new Lecture(2L, "스프링", "김선생님", 30, 29, LectureStatus.OPEN);

        // When
        boolean isSameLecture = lecture1.isSameLecture(lecture2);

        // Then
        assertTrue(isSameLecture);
    }

    @Test
    void 다른_강의_비교() {
        //given
        Lecture lecture1 = new Lecture(1L, "스프링", "김선생님", 30, 29, LectureStatus.OPEN);
        Lecture lecture2 = new Lecture(2L, "자바", "김선생님", 30, 29, LectureStatus.OPEN);

        //when
        boolean isSameLecture = lecture1.isSameLecture(lecture2);

        //then
        assertFalse(isSameLecture);
    }

    @Test
    void 같은_스케줄_비교() {
        //given
        Lecture lecture = new Lecture(1L, "스프링", "김선생님", 30, 29, LectureStatus.OPEN);
        Schedule schedule1 = new Schedule(1L, LocalDateTime.of(2024, 6, 13, 10, 0), lecture);
        Schedule schedule2 = new Schedule(2L, LocalDateTime.of(2024, 6, 13, 9, 0), lecture);

        //when
        boolean isSameSchedule = schedule1.isSameSchedule(schedule2);

        //then
        assertTrue(isSameSchedule);
    }

    @Test
    void 다른_스케줄_비교() {
        //given
        Lecture lecture = new Lecture(1L, "스프링", "김선생님", 30, 29, LectureStatus.OPEN);
        Schedule schedule1 = new Schedule(1L, LocalDateTime.of(2024, 6, 13, 10, 0), lecture);
        Schedule schedule2 = new Schedule(2L, LocalDateTime.of(2024, 6, 13, 12, 0), lecture);

        //when
        boolean isSameSchedule = schedule1.isSameSchedule(schedule2);

        //then
        assertFalse(isSameSchedule);
    }
}
