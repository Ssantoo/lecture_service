package com.test.lecture.lecture.service.port;

import com.test.lecture.lecture.domain.LectureApply;

public interface LectureApplyRepository {
    boolean existsByUserIdAndScheduleId(Long userId, Long lectureId);

    void save(LectureApply lectureApply);
}
