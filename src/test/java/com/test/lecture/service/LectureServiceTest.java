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
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
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
                        new Lecture(2L, "tdd수업", "김선생님", 20, 15, LectureStatus.PREPARING)),
                new Schedule(3L, LocalDateTime.of(2024, 6, 20, 10, 0),
                        new Lecture(3L, "알고리즘", "박선생님", 25, 20, LectureStatus.CLOSED))
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


    @Test
    @Transactional
    void 선착순_체크() throws InterruptedException {
        //given
        //테스트 10명유저
        List<User> users = new ArrayList<>();
        for (long i = 1; i <= 10; i++) {
            User user = User.builder()
                    .id(i)
                    .name("조현재" + i)
                    .build();
            users.add(user);
        }

        Lecture lecture = Lecture.builder()
                .id(1L)
                .lectureName("스프링")
                .teacherName("김선생님")
                .totalStudents(30)
                .currentStudents(27)
                .lectureStatus(LectureStatus.OPEN)
                .build();

        Schedule schedule = Schedule.builder()
                .id(1L)
                .time(LocalDateTime.of(2024, 6, 13, 10, 0))
                .lecture(lecture)
                .build();

        given(scheduleRepository.findByIdWithLock(anyLong())).willReturn(Optional.of(schedule));
        will(invocation -> {
            LectureApply lectureApply = invocation.getArgument(0);
            return lectureApply;
        }).given(lectureApplyRepository).save(any(LectureApply.class));

        for (User user : users) {
            given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
            given(lectureApplyRepository.existsByUserIdAndScheduleId(user.getId(), schedule.getId())).willReturn(false);
        }

        CountDownLatch latch = new CountDownLatch(10); // 동시 실행을 위한 CountDownLatch를 생성
        ExecutorService executor = Executors.newFixedThreadPool(10); // 10개의 스레드를 가진 스레드 풀을 생성
        List<Exception> exceptions = new ArrayList<>(); //예외처리

        //when
        for (User user : users) {
            executor.submit(() -> { // 각 유저에 대해 강의 신청을 시도하는 작업을 스레드 풀에 제출
                try {
                    lectureService.applyLecture(user.getId(), schedule.getId()); // 강의 신청을 수행
                } catch (Exception e) {
                    exceptions.add(e); // 예외가 발생하면 리스트에 추가
                } finally {
                    latch.countDown(); // 작업이 끝나면 CountDownLatch의 카운트를 줄인다
                }
            });
        }
        latch.await();
        executor.shutdown();

        //then
        assertEquals(30, lecture.getCurrentStudents());
        assertEquals(7, exceptions.size());
        exceptions.forEach(exception -> {
            assertTrue(
                    exception instanceof LectureAlreadyFullException ||
                            exception instanceof AlreadyApplyException,
                    "예외발생" + exception.getClass().getName()
            );
        });

        then(scheduleRepository).should(times(10)).findByIdWithLock(anyLong()); //findByIdWithLock 메서드가 10번 호출되었는지 검증
        then(userRepository).should(times(10)).findById(anyLong()); // UserRepository의 findById 메서드가 10번 호출되었는지 검증
        then(lectureApplyRepository).should(times(10)).existsByUserIdAndScheduleId(anyLong(), anyLong()); // LectureApplyRepository의 existsByUserIdAndScheduleId 메서드가 10번 호출되었는지 검증
        then(lectureApplyRepository).should(times(3)).save(any(LectureApply.class)); // LectureApplyRepository의 save 메서드가 3번 호출되었는지 검증
    }

    @Test
    @Transactional
    void 동시성_테스트_연습() throws InterruptedException, ExecutionException, TimeoutException {
        //given
        long scheduleId = 1L;
        Lecture lecture = new Lecture(1L, "스프링", "김선생님", 30, 29, LectureStatus.OPEN);
        Schedule schedule = new Schedule(scheduleId, LocalDateTime.of(2024, 6, 13, 10, 0), lecture);
        User[] users = {
                new User(1L, "User 1"),
                new User(2L, "User 2"),
                new User(3L, "User 3"),
                new User(4L, "User 4"),
                new User(5L, "User 5"),
                new User(6L, "User 6"),
                new User(7L, "User 7"),
                new User(8L, "User 8"),
                new User(9L, "User 9"),
                new User(10L, "User 10")
        };

        // scheduleRepository에서 스케줄을 비관적 락으로 조회하도록 설정
        given(scheduleRepository.findByIdWithLock(scheduleId)).willReturn(Optional.of(schedule));

        for (User user : users) {
            given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
            given(lectureApplyRepository.existsByUserIdAndScheduleId(user.getId(), scheduleId)).willReturn(false);
            // 각 User ID와 Schedule ID에 대해 이미 신청된 기록이 없도록 설정
        }

        // 10명의 유저가 동시 신청을 비동기로 실행하여 CompletableFuture 리스트로 생성
        List<CompletableFuture<Optional<LectureApply>>> futures = Arrays.stream(users)
                .map(user -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return Optional.ofNullable(lectureService.applyLecture(user.getId(), scheduleId));
                    } catch (LectureAlreadyFullException | AlreadyApplyException e) {
                        return Optional.<LectureApply>empty(); // 예외 발생 시 Optional.empty() 반환
                    }
                }))
                .collect(Collectors.toList());

        // 모든 CompletableFuture를 하나의 CompletableFuture로 합침
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        //when
        // 10초 내에 모든 비동기 작업 완료 대기
        allFutures.get(10, TimeUnit.SECONDS);

        //then
        // 각 CompletableFuture 결과를 Optional로 받아 LectureApply 객체 리스트로 변환
        List<LectureApply> results = futures.stream()
                .map(future -> {
                    try {
                        return future.get();   // 각 CompletableFuture 결과 가져오기
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                })
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        verify(userRepository, times(10)).findById(anyLong());  // userRepository의 findById 호출 횟수 검증
        verify(scheduleRepository, times(10)).findByIdWithLock(scheduleId);  // scheduleRepository의 findByIdWithLock 호출 횟수 검증
        verify(lectureApplyRepository, times(10)).existsByUserIdAndScheduleId(anyLong(), eq(scheduleId));
        // lectureApplyRepository의 existsByUserIdAndScheduleId 호출 횟수 검증

        // 성공적으로 신청된 유저들 확인
        int successfulApplications = results.size();
        assertEquals(1, successfulApplications); // 성공적으로 신청된 유저가 1명인지 검증
        if (successfulApplications == 1) {
            System.out.println("성공적으로 신청된 유저: " + results.get(0).getUser().getName());
        }
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
