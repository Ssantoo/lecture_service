package com.test.lecture.lecture.controller.port;

import com.test.lecture.lecture.domain.LectureApply;
import com.test.lecture.lecture.domain.Schedule;

import java.util.List;

public interface LectureService {

    List<Schedule> getAllLectures();

    LectureApply checkUserLectureApplication(Long userId, Long scheduleId);

    LectureApply applyLecture(Long userId, Long scheduleId);
}
