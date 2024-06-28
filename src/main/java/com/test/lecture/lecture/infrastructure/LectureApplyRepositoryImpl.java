package com.test.lecture.lecture.infrastructure;

import com.test.lecture.lecture.domain.LectureApply;
import com.test.lecture.lecture.infrastructure.entity.LectureApplyEntity;
import com.test.lecture.lecture.service.port.LectureApplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LectureApplyRepositoryImpl implements LectureApplyRepository {

    private final LectureApplyJpaRepository lectureApplyJpaRepository;


    @Override
    public boolean existsByUserIdAndScheduleId(Long userId, Long lectureId) {
        return lectureApplyJpaRepository.existsByUserIdAndScheduleId(userId, lectureId);
    }

    @Override
    public void save(LectureApply lectureApply) {
        lectureApplyJpaRepository.save(LectureApplyEntity.from(lectureApply));
    }
}
