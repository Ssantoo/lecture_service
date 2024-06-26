package com.test.lecture.lecture.service;

import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.LectureApplication;
import com.test.lecture.lecture.domain.Schedule;
import com.test.lecture.lecture.domain.User;
import com.test.lecture.lecture.service.port.LectureApplicationRepository;
import com.test.lecture.lecture.service.port.LectureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {

    @InjectMocks
    private LectureServiceImpl lectureService;

    @Mock
    private LectureApplicationRepository lectureApplicationRepository;
    @Mock
    private LectureRepository lectureRepository;

    @Test
    void 모든_강의를_조회한다() {

        //given
        List<Lecture> lectures = Arrays.asList(
                new Lecture(1L, "스프링", "김선생님", 30, Arrays.asList(
                        new Schedule(1L, LocalDateTime.of(2024, 6, 13, 10, 0), null))),
                new Lecture(2L, "tdd수업", "김선생님", 20, Arrays.asList(
                        new Schedule(2L, LocalDateTime.of(2024, 6, 14, 14, 0), null))),
                new Lecture(3L, "알고리즘", "박선생님", 25, Arrays.asList(
                        new Schedule(3L, LocalDateTime.of(2024, 6, 10, 9, 0), null)))
        );

        given(lectureRepository.findAll()).willReturn(lectures);

        //when
        List<Lecture> result = lectureService.getAllLectures();
        //then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getName()).isEqualTo("스프링");
        assertThat(result.get(1).getName()).isEqualTo("tdd수업");
    }

    @Test
    void 사용자가_특정_강의에_신청한_경우_강의_신청_목록을_조회한다() {
        // given
        long userId = 1L;

        User user = User.builder().id(userId).name("조현재").build();

        List<LectureApplication> applications = Arrays.asList(
                LectureApplication.builder()
                        .id(1L)
                        .user(user)
                        .lecture(new Lecture(1L, "스프링", "김선생님", 30, Arrays.asList(
                                new Schedule(1L, LocalDateTime.of(2024, 6, 13, 10, 0), null))))
                        .schedule(new Schedule(1L, LocalDateTime.of(2024, 6, 13, 10, 0), null))
                        .applicationDate(LocalDate.now())
                        .build(),
                LectureApplication.builder()
                        .id(2L)
                        .user(user)
                        .lecture(new Lecture(2L, "알고리즘", "박선생님", 25, Arrays.asList(
                                new Schedule(2L, LocalDateTime.of(2024, 6, 10, 9, 0), null))))
                        .schedule(new Schedule(2L, LocalDateTime.of(2024, 6, 10, 9, 0), null))
                        .applicationDate(LocalDate.now())
                        .build()
        );

        given(lectureApplicationRepository.findByUserId(userId)).willReturn(applications);

        // when
        List<LectureApplication> result = lectureService.getApplicationsByUserId(userId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getLecture().getName()).isEqualTo("스프링");
        assertThat(result.get(1).getLecture().getName()).isEqualTo("알고리즘");

        assertThat(result).extracting("lecture.name").contains("스프링", "알고리즘");
        assertThat(result).extracting("lecture.teacher").contains("김선생님", "박선생님");
    }

    @Test
    void 사용자가_특정_강의에_신청하지_않은_경우_빈_목록을_반환한다() {
        // given
        long userId = 1L;

        given(lectureApplicationRepository.findByUserId(userId)).willReturn(Arrays.asList());

        // when
        List<LectureApplication> result = lectureService.getApplicationsByUserId(userId);

        // then
        assertThat(result).isEmpty();
    }




}
