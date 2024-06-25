package com.test.lecture.lecture.service;

import com.test.lecture.common.domain.exception.ResourceNotFoundException;
import com.test.lecture.history.service.port.HistoryRepository;
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

    private final HistoryRepository historyRepository;

    @Override
    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    @Override
    public boolean userLectureCheck(long lectureId, long userId) throws ResourceNotFoundException {
        if (!historyRepository.existsByLectureIdAndUserId(lectureId, userId)) {
            throw new ResourceNotFoundException("유저 또는 강의", userId);
        }
        return true;
    }
}
