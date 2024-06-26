package com.test.lecture.lecture.controller.port;

import com.test.lecture.common.domain.exception.ResourceNotFoundException;
import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.LectureApplication;

import java.util.List;

public interface LectureService {
    List<Lecture> getAllLectures();

    List<LectureApplication> getApplicationsByUserId(Long userId);
}
