package com.test.user.infrastructure.entity;

import com.test.user.domain.User;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "student_name")
    private String name;

    public static UserEntity from(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.id = user.getId();
        userEntity.name = user.getName();
        return userEntity;
    }

    public User toModel() {
        return User.builder()
                .id(id)
                .name(name)
                .build();
    };
}
