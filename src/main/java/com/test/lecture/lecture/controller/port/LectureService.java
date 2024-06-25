package com.test.lecture.lecture.controller.port;

import com.test.lecture.common.domain.exception.ResourceNotFoundException;
import com.test.lecture.lecture.domain.Lecture;

import java.util.List;

public interface LectureService {
    List<Lecture> getAllLectures();

    boolean userLectureCheck(long lectureId, long userId) throws ResourceNotFoundException;;
}
