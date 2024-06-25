package com.test.lecture.lecture.service;

import com.test.lecture.lecture.controller.port.LectureService;
import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.service.port.LectureRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Builder
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;

    @Override
    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }
}
