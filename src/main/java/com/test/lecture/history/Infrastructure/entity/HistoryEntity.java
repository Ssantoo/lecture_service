package com.test.lecture.history.Infrastructure.entity;

import com.test.lecture.history.domain.History;
import com.test.lecture.lecture.infrastructure.entity.LectureEntity;
import com.test.user.infrastructure.entity.UserEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lecture_history")
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = false)
    private LectureEntity lecture;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "applied_time", nullable = false)
    private LocalDateTime appliedTime;

    public static HistoryEntity from(History history) {
        HistoryEntity historyEntity = new HistoryEntity();
        historyEntity.id = history.getId();
        historyEntity.lecture = LectureEntity.from(history.getLecture());
        historyEntity.user = UserEntity.from(history.getUser());
        historyEntity.appliedTime = history.getAppliedTime();
        return historyEntity;
    }

    public History toModel() {
        return History.builder()
                .id(id)
                .lecture(lecture.toModel())
                .user(user.toModel())
                .appliedTime(appliedTime)
                .build();
    }

}
