package com.test.lecture.lecture.service;

import com.test.lecture.lecture.controller.port.LectureService;
import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.LectureApplication;
import com.test.lecture.lecture.domain.Schedule;
import com.test.lecture.lecture.infrastructure.entity.ScheduleEntity;
import com.test.lecture.lecture.service.port.LectureApplicationRepository;
import com.test.lecture.lecture.service.port.LectureRepository;
import com.test.lecture.lecture.service.port.ScheduleRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Builder
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;

    private final LectureApplicationRepository lectureApplicationRepository;

    private final ScheduleRepository scheduleRepository;

    @Override
    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    @Override
    public List<LectureApplication> getApplicationsByUserId(Long userId) {
        return lectureApplicationRepository.findByUserId(userId);
    }


}
