package com.test.lecture.lecture.service.port;

import com.test.lecture.lecture.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long userId);
}
