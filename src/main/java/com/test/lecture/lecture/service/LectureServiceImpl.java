package com.test.lecture.lecture.service;

import com.test.lecture.common.exception.AlreadyApplyException;
import com.test.lecture.common.exception.LectureAlreadyFullException;
import com.test.lecture.common.exception.ResourceNotFoundException;
import com.test.lecture.lecture.domain.LectureApply;
import com.test.lecture.lecture.controller.port.LectureService;
import com.test.lecture.lecture.domain.Schedule;
import com.test.lecture.lecture.domain.User;
import com.test.lecture.lecture.service.port.LectureApplyRepository;
import com.test.lecture.lecture.service.port.LectureRepository;
import com.test.lecture.lecture.service.port.ScheduleRepository;
import com.test.lecture.lecture.service.port.UserRepository;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Builder
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;

    private final ScheduleRepository scheduleRepository;

    private final UserRepository userRepository;

    private final LectureApplyRepository lectureApplyRepository;
    private final Object lock = new Object();
    @Override
    public List<Schedule> getAllLectures() {
        return  scheduleRepository.findAll();
    }

    @Override
    public LectureApply checkUserLectureApplication(Long userId, Long scheduleId) {
        final User user = findByUserId(userId);
        final Schedule schedule = findByScheduleId(scheduleId);
//        boolean isApplied = lectureApplyRepository.existsByUserIdAndScheduleId(userId, scheduleId);
        boolean isApplied = existsByUserIdAndScheduleId(userId, scheduleId);
        return LectureApply.update(isApplied, user, schedule);
    }

    @Override
    @Transactional
    public LectureApply applyLecture(Long userId, Long scheduleId) {
//        User user = findByUserId(userId);
//        //Schedule schedule = findByScheduleId(scheduleId);
//        Schedule schedule = findByScheduleIdWithLock(scheduleId);
//
//        checkAlreadyApply(userId, scheduleId);
//        lectureAlreadyFull(schedule);
//        incrementCurrentStudents(schedule);
//
//        return createLectureApply(user, schedule);
        synchronized (lock) { // 동기화 블록
            User user = findByUserId(userId);
            Schedule schedule = findByScheduleIdWithLock(scheduleId);

            checkAlreadyApply(userId, scheduleId);
            lectureAlreadyFull(schedule);
            incrementCurrentStudents(schedule);

            return createLectureApply(user, schedule);
        }
    }



    private User findByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("유저", userId));
    }

    private Schedule findByScheduleId(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("강의", scheduleId));
    }

    private boolean existsByUserIdAndScheduleId(Long userId, Long scheduleId) {
        return lectureApplyRepository.existsByUserIdAndScheduleId(userId, scheduleId);
    }

    private void checkAlreadyApply(Long userId, Long scheduleId) {
        if(existsByUserIdAndScheduleId(userId, scheduleId)) {
            throw new AlreadyApplyException("이미 지원한 강의입니다.");
        }
    }

    private void lectureAlreadyFull(Schedule schedule){
        if(schedule.getLecture().isFull()) {
            throw new LectureAlreadyFullException("강의 자리가 꽉 찼습니다.");
        }
    }

    private void incrementCurrentStudents(Schedule schedule) {
        schedule.getLecture().incrementCurrentStudents();
        scheduleRepository.save(schedule);
    }

    private LectureApply createLectureApply(User user, Schedule schedule) {
        LectureApply lectureApply = LectureApply.create(user, schedule);
        lectureApplyRepository.save(lectureApply);
        return lectureApply;
    }

    private Schedule findByScheduleIdWithLock(Long scheduleId) {
        return scheduleRepository.findByIdWithLock(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("강의", scheduleId));
    }
}
