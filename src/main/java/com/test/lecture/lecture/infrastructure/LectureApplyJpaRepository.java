package com.test.lecture.lecture.infrastructure;

import com.test.lecture.lecture.infrastructure.entity.LectureApplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureApplyJpaRepository extends JpaRepository<LectureApplyEntity, Long> {

    boolean existsByUserIdAndScheduleId(Long userId, Long lectureId);
}
