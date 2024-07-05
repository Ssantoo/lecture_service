package com.test.lecture.lecture.infrastructure;

import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.infrastructure.entity.LectureEntity;
import com.test.lecture.lecture.service.port.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository {

    private final LectureJpaRepository lectureJpaRepository;


}
