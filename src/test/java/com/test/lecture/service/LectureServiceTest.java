package com.test.lecture.service;

import com.test.lecture.common.exception.AlreadyApplyException;
import com.test.lecture.common.exception.LectureAlreadyFullException;
import com.test.lecture.common.exception.ResourceNotFoundException;
import com.test.lecture.lecture.domain.*;
import com.test.lecture.lecture.service.LectureServiceImpl;
import com.test.lecture.lecture.service.port.LectureApplyRepository;
import com.test.lecture.lecture.service.port.LectureRepository;
import com.test.lecture.lecture.service.port.ScheduleRepository;
import com.test.lecture.lecture.service.port.UserRepository;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {

    @InjectMocks
    private LectureServiceImpl lectureService;

    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private LectureRepository lectureRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LectureApplyRepository lectureApplyRepository;

    //강의 리스트 조회
    @Test
    void 모든_강의를_조회한다() {
        // given
        List<Schedule> schedules = Arrays.asList(
                new Schedule(1L, LocalDateTime.of(2024, 6, 20, 10, 0),
                        new Lecture(1L, "스프링", "김선생님", 30, 25, LectureStatus.OPEN)),
                new Schedule(2L, LocalDateTime.of(2023, 6, 20, 10, 0),
                        new Lecture(2L, "tdd수업", "김선생님",  20, 15, LectureStatus.PREPARING)),
                new Schedule(3L, LocalDateTime.of(2024, 6, 20, 10, 0),
                        new Lecture(3L, "알고리즘", "박선생님",  25, 20, LectureStatus.CLOSED))
        );
        when(scheduleRepository.findAll()).thenReturn(schedules);

        // when
        List<Schedule> result = lectureService.getAllLectures();

        // then
        assertEquals(3, result.size());
        assertTrue(result.containsAll(schedules));
        verify(scheduleRepository, times(1)).findAll();
    }

    //강의 리스트가 없을때 빈리스트를 던져준다
    @Test
    void 강의_리스트에_아무것도_없다면_빈_리스트를_던져준다() {
        // given
        when(scheduleRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<Schedule> result = lectureService.getAllLectures();

        // then
        assertTrue(result.isEmpty());
        verify(scheduleRepository, times(1)).findAll();
    }

    //findByUserId 유저가 존재하지 않을때
    @Test
    void 유저가_존재하지_않을_때_조회를_실패한다() {
        // given
        long userId = 1L;
        long scheduleId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(ResourceNotFoundException.class, () -> {
            lectureService.checkUserLectureApplication(userId, scheduleId);
        });
        verify(userRepository, times(1)).findById(userId);
    }

    //scheduleId 강의가 존재하지 않을때
    @Test
    void 강의가_존재하지_않을_때_조회를_실패한다() {
        // given
        long userId = 1L;
        long scheduleId = 1L;
        User user = new User(userId, "조현재");
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(scheduleRepository.findById(scheduleId)).willReturn(Optional.empty());

        // when
        // then
        assertThrows(ResourceNotFoundException.class, () -> {
            lectureService.checkUserLectureApplication(userId, scheduleId);
        });
        verify(userRepository, times(1)).findById(userId);
        verify(scheduleRepository, times(1)).findById(scheduleId);
    }

    //특정 강의 신청 실패했을때
    @Test
    void 특정유저가_특정강의_신청_실패_결과가_나온다() {
        // given
        long userId = 1L;
        long scheduleId = 1L;
        User user = new User(userId, "조현재");
        Lecture lecture = new Lecture(1L, "스프링", "김선생님", 30, 25, LectureStatus.OPEN);
        Schedule schedule = new Schedule(scheduleId, LocalDateTime.of(2024, 6, 13, 10, 0), lecture);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));
        given(lectureApplyRepository.existsByUserIdAndScheduleId(userId, scheduleId)).willReturn(false);

        // when
        LectureApply result = lectureService.checkUserLectureApplication(userId, scheduleId);

        // then
        assertFalse(result.isApplied());
        assertEquals(user, result.getUser());
        assertEquals(schedule, result.getSchedule());
        verify(lectureApplyRepository, times(1)).existsByUserIdAndScheduleId(userId, scheduleId);
    }

    //특정 강의 신청 성공했을때
    @Test
    void 특정유저가_특정강의_신청_성공_결과가_나온다() {
        // given
        long userId = 1L;
        long scheduleId = 1L;
        User user = new User(userId, "조현재");
        Lecture lecture = new Lecture(1L, "스프링", "김선생님", 30, 25, LectureStatus.OPEN);
        Schedule schedule = new Schedule(scheduleId, LocalDateTime.of(2024, 6, 13, 10, 0), lecture);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(scheduleRepository.findById(scheduleId)).willReturn(Optional.of(schedule));
        given(lectureApplyRepository.existsByUserIdAndScheduleId(userId, scheduleId)).willReturn(true);

        // when
        LectureApply result = lectureService.checkUserLectureApplication(userId, scheduleId);

        // then
        assertTrue(result.isApplied());
        assertEquals(user, result.getUser());
        assertEquals(schedule, result.getSchedule());
        verify(lectureApplyRepository, times(1)).existsByUserIdAndScheduleId(userId, scheduleId);
    }

    //이미 지원한 강의라면
    @Test
    void 이미_지원한_강의에_대해_예외를_발생시킨다() {
        // given
        long userId = 1L;
        long scheduleId = 1L;
        User user = new User(userId, "조현재");
        Lecture lecture = new Lecture(1L, "스프링", "김선생님", 30, 25, LectureStatus.OPEN);
        Schedule schedule = new Schedule(scheduleId, LocalDateTime.of(2024, 6, 13, 10, 0), lecture);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(scheduleRepository.findByIdWithLock(scheduleId)).willReturn(Optional.of(schedule));
        given(lectureApplyRepository.existsByUserIdAndScheduleId(userId, scheduleId)).willReturn(true);

        // when
        // then
        assertThrows(AlreadyApplyException.class, () -> {
            lectureService.applyLecture(userId, scheduleId);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(scheduleRepository, times(1)).findByIdWithLock(scheduleId);
        verify(lectureApplyRepository, times(1)).existsByUserIdAndScheduleId(userId, scheduleId);
    }

    //이미 자리가 꽉찼을때
    @Test
    void 강의자리가_꽉_찼을_때_예외를_발생시킨다() {
        // given
        long userId = 1L;
        long scheduleId = 1L;
        User user = new User(userId, "조현재");
        Lecture lecture = new Lecture(1L, "스프링", "김선생님", 30, 30, LectureStatus.OPEN);
        Schedule schedule = new Schedule(scheduleId, LocalDateTime.of(2024, 6, 13, 10, 0), lecture);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(scheduleRepository.findByIdWithLock(scheduleId)).willReturn(Optional.of(schedule));
        given(lectureApplyRepository.existsByUserIdAndScheduleId(userId, scheduleId)).willReturn(false);

        // when
        // then
        assertThrows(LectureAlreadyFullException.class, () -> {
            lectureService.applyLecture(userId, scheduleId);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(scheduleRepository, times(1)).findByIdWithLock(scheduleId);
        verify(lectureApplyRepository, times(1)).existsByUserIdAndScheduleId(userId, scheduleId);
    }








    //동시에..? 이게 맞나..
    @RepeatedTest(10)
    void 동시성_테스트() throws InterruptedException, ExecutionException, TimeoutException {
        // given
        long scheduleId = 1L;
        Lecture lecture = new Lecture(1L, "스프링", "김선생님", 30, 29, LectureStatus.OPEN);
        Schedule schedule = new Schedule(scheduleId, LocalDateTime.of(2024, 6, 13, 10, 0), lecture);
        User user1 = new User(1L, "User 1");
        User user2 = new User(2L, "User 2");
        User user3 = new User(3L, "User 3");
        User user4 = new User(4L, "User 4");
        User user5 = new User(5L, "User 5");
        User user6 = new User(6L, "User 6");
        User user7 = new User(7L, "User 7");
        User user8 = new User(8L, "User 8");
        User user9 = new User(9L, "User 9");
        User user10 = new User(10L, "User 10");

        given(scheduleRepository.findByIdWithLock(scheduleId)).willReturn(Optional.of(schedule));
        given(lectureApplyRepository.existsByUserIdAndScheduleId(1L, scheduleId)).willReturn(false);
        given(lectureApplyRepository.existsByUserIdAndScheduleId(2L, scheduleId)).willReturn(false);
        given(lectureApplyRepository.existsByUserIdAndScheduleId(3L, scheduleId)).willReturn(false);
        given(lectureApplyRepository.existsByUserIdAndScheduleId(4L, scheduleId)).willReturn(false);
        given(lectureApplyRepository.existsByUserIdAndScheduleId(5L, scheduleId)).willReturn(false);
        given(lectureApplyRepository.existsByUserIdAndScheduleId(6L, scheduleId)).willReturn(false);
        given(lectureApplyRepository.existsByUserIdAndScheduleId(7L, scheduleId)).willReturn(false);
        given(lectureApplyRepository.existsByUserIdAndScheduleId(8L, scheduleId)).willReturn(false);
        given(lectureApplyRepository.existsByUserIdAndScheduleId(9L, scheduleId)).willReturn(false);
        given(lectureApplyRepository.existsByUserIdAndScheduleId(10L, scheduleId)).willReturn(false);

        given(userRepository.findById(1L)).willReturn(Optional.of(user1));
        given(userRepository.findById(2L)).willReturn(Optional.of(user2));
        given(userRepository.findById(3L)).willReturn(Optional.of(user3));
        given(userRepository.findById(4L)).willReturn(Optional.of(user4));
        given(userRepository.findById(5L)).willReturn(Optional.of(user5));
        given(userRepository.findById(6L)).willReturn(Optional.of(user6));
        given(userRepository.findById(7L)).willReturn(Optional.of(user7));
        given(userRepository.findById(8L)).willReturn(Optional.of(user8));
        given(userRepository.findById(9L)).willReturn(Optional.of(user9));
        given(userRepository.findById(10L)).willReturn(Optional.of(user10));

        // when
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> lectureService.applyLecture(1L, scheduleId));
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> lectureService.applyLecture(2L, scheduleId));
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> lectureService.applyLecture(3L, scheduleId));
        CompletableFuture<Void> future4 = CompletableFuture.runAsync(() -> lectureService.applyLecture(4L, scheduleId));
        CompletableFuture<Void> future5 = CompletableFuture.runAsync(() -> lectureService.applyLecture(5L, scheduleId));
        CompletableFuture<Void> future6 = CompletableFuture.runAsync(() -> lectureService.applyLecture(6L, scheduleId));
        CompletableFuture<Void> future7 = CompletableFuture.runAsync(() -> lectureService.applyLecture(7L, scheduleId));
        CompletableFuture<Void> future8 = CompletableFuture.runAsync(() -> lectureService.applyLecture(8L, scheduleId));
        CompletableFuture<Void> future9 = CompletableFuture.runAsync(() -> lectureService.applyLecture(9L, scheduleId));
        CompletableFuture<Void> future10 = CompletableFuture.runAsync(() -> lectureService.applyLecture(10L, scheduleId));

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                future1, future2, future3, future4, future5, future6, future7, future8, future9, future10);

        ExecutionException exception = assertThrows(ExecutionException.class, () -> allFutures.get(10, TimeUnit.SECONDS));
        Throwable cause = exception.getCause();

        assertTrue(cause instanceof LectureAlreadyFullException || cause instanceof AlreadyApplyException);

        verify(userRepository, times(10)).findById(anyLong());
        verify(scheduleRepository, times(10)).findByIdWithLock(scheduleId);
        verify(lectureApplyRepository, times(10)).existsByUserIdAndScheduleId(anyLong(), eq(scheduleId));
    }



}
