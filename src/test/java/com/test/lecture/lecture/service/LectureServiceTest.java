package com.test.lecture.lecture.service;

import com.test.lecture.common.domain.exception.ResourceNotFoundException;
import com.test.lecture.history.Infrastructure.HistoryRepositoryImpl;
import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.LectureStatus;
import com.test.lecture.lecture.service.port.LectureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {

    @InjectMocks
    private LectureServiceImpl lectureService;

    @Mock
    private HistoryRepositoryImpl historyRepository;

    @Mock
    private LectureRepository lectureRepository;

    @Test
    void 모든_강의를_조회한다() {

        //given
        List<Lecture> lectures = Arrays.asList(
                new Lecture(1L, "스프링", 30, 0, LocalDateTime.of(2024, 6, 13, 10, 0), LectureStatus.PREPARING),
                new Lecture(2L, "tdd수업", 20, 20, LocalDateTime.of(2024, 6, 14, 14, 0), LectureStatus.OPEN),
                new Lecture(3L, "알고리즘", 25, 25, LocalDateTime.of(2024, 6, 10, 9, 0), LectureStatus.CLOSED)
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
    void 사용자가_강의를_성공하여_히스토리에_있는_경우() {
        //given
        long lectureId = 1L;
        long userId = 1L;

        given(historyRepository.existsByLectureIdAndUserId(anyLong(), anyLong())).willReturn(true);

        //when
        boolean result = lectureService.userLectureCheck(anyLong(), anyLong());

        //then
        assertThat(result).isTrue();

    }

    @Test
    void 사용자가_강의를_신청_실패하여_히스토리에_없는_경우() {
        //given
        long lectureId = 1L;
        long userId = 1L;

        given(historyRepository.existsByLectureIdAndUserId(anyLong(), anyLong())).willReturn(false);

        //when
        //then
        assertThatThrownBy(() -> lectureService.userLectureCheck(lectureId, userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("유저 또는 강의에서 ID " + userId + "를 찾을 수 없습니다.");
    }

    @Test
    void 사용자가_존재하지_않을_경우_히스토리_조회_실패한다() {
        // given
        long lectureId = 1L;
        long userId = 0L;

        given(historyRepository.existsByLectureIdAndUserId(anyLong(), anyLong())).willReturn(false);

        //when
        //then
        assertThatThrownBy(() -> lectureService.userLectureCheck(lectureId, userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("유저 또는 강의에서 ID " + userId + "를 찾을 수 없습니다.");

    }

    @Test
    void 강의가_존재하지_않을_경우_히스토리_조회_실패한다() {
        // given
        long lectureId = 0L;
        long userId = 1L;

        given(historyRepository.existsByLectureIdAndUserId(anyLong(), anyLong())).willReturn(false);

        //when
        //then
        assertThatThrownBy(() -> lectureService.userLectureCheck(lectureId, userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("유저 또는 강의에서 ID " + userId + "를 찾을 수 없습니다.");
    }




}
