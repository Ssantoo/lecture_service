package com.test.lecture.lecture.infrastructure;

import com.test.lecture.lecture.infrastructure.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
}
