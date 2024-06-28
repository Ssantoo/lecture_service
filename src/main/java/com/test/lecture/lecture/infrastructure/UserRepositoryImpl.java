package com.test.lecture.lecture.infrastructure;

import com.test.lecture.lecture.domain.User;
import com.test.lecture.lecture.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import com.test.lecture.lecture.infrastructure.entity.UserEntity;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId).map(UserEntity::toModel);
    }
}
