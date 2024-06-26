package com.test.lecture.lecture.infrastructure;

import com.test.lecture.lecture.infrastructure.entity.LectureApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureApplicationJpaRepository extends JpaRepository<LectureApplicationEntity, Long> {
}
