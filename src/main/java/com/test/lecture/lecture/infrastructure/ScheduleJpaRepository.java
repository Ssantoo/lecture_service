package com.test.lecture.lecture.infrastructure;

import com.test.lecture.lecture.infrastructure.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScheduleJpaRepository extends CrudRepository<ScheduleEntity, Long> {

    @Query("SELECT s FROM ScheduleEntity s LEFT JOIN FETCH s.lecture")
    List<ScheduleEntity> findAllWithLectures();

}
