package com.test.lecture.lecture.infrastructure;

import com.test.lecture.lecture.infrastructure.entity.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureJpaRepository extends JpaRepository<LectureEntity, Long> {

}
