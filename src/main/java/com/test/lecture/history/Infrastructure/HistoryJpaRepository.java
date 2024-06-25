package com.test.lecture.history.Infrastructure;

import com.test.lecture.history.Infrastructure.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryJpaRepository extends JpaRepository<HistoryEntity, Long> {
    boolean existsByLectureIdAndUserId(long lectureId, long userId);
}
