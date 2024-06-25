package com.test.lecture.history.Infrastructure;

import com.test.lecture.history.service.port.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HistoryRepositoryImpl implements HistoryRepository {

    private final HistoryJpaRepository historyJpaRepository;


    @Override
    public boolean existsByLectureIdAndUserId(long lectureId, long userId) {
        return historyJpaRepository.existsByLectureIdAndUserId(lectureId, userId);
    }
}
