package com.test.lecture.lecture.infrastructure;

import com.test.lecture.lecture.domain.LectureApplication;
import com.test.lecture.lecture.infrastructure.entity.LectureApplicationEntity;
import com.test.lecture.lecture.infrastructure.entity.LectureEntity;
import com.test.lecture.lecture.service.port.LectureApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LectureApplicationRepositoryImpl implements LectureApplicationRepository {

    private final LectureApplicationJpaRepository lectureApplicationJpaRepository;

    @Override
    public List<LectureApplication> findByUserId(Long userId) {
        return lectureApplicationJpaRepository.findAll().stream().map(LectureApplicationEntity::toModel).toList();
    }
}
