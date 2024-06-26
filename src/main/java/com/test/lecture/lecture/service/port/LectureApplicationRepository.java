package com.test.lecture.lecture.service.port;

import com.test.lecture.lecture.domain.LectureApplication;

import java.util.List;

public interface LectureApplicationRepository {
    List<LectureApplication> findByUserId(Long userId);
}
