package com.test.lecture.history.service.port;

public interface HistoryRepository {
    boolean existsByLectureIdAndUserId(long lectureId, long userId);
}
