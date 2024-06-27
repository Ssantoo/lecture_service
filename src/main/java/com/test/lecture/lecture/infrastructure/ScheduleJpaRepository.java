package com.test.lecture.lecture.infrastructure;

import com.test.lecture.lecture.infrastructure.entity.ScheduleEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.nio.channels.FileChannel;
import java.util.Optional;

public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT scheduleEntity FROM ScheduleEntity scheduleEntity WHERE scheduleEntity.id = :scheduleId")
    Optional<ScheduleEntity> findByIdWithLock(@Param("scheduleId") Long scheduleId);
}
