package com.test.lecture.lecture.controller.port;

import com.test.lecture.lecture.domain.Lecture;

import java.util.List;

public interface LectureService {
    List<Lecture> getAllLectures();
}
