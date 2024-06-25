package com.test.lecture.lecture.service.port;

import com.test.lecture.lecture.domain.Lecture;

import java.util.List;

public interface LectureRepository {
    List<Lecture> findAll();
}
