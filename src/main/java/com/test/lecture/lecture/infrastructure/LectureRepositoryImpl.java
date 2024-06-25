package com.test.lecture.lecture.infrastructure;


import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.infrastructure.entity.LectureEntity;
import com.test.lecture.lecture.service.port.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository {

    private final LectureJpaRepository lectureJpaRepository;

    @Override
    public List<Lecture> findAll() {
        return lectureJpaRepository.findAll().stream().map(LectureEntity::toModel).toList();
    }
}
