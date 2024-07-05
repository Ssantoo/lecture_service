package com.test.lecture.lecture.infrastructure.entity;

import com.test.lecture.lecture.domain.LectureApply;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lecture_apply")
public class LectureApplyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private ScheduleEntity schedule;

    public static LectureApplyEntity from(LectureApply lectureApply) {
        LectureApplyEntity lectureApplyEntity = new LectureApplyEntity();
        lectureApplyEntity.user = UserEntity.from(lectureApply.getUser());
        lectureApplyEntity.schedule = ScheduleEntity.from(lectureApply.getSchedule());
        return  lectureApplyEntity;
    }
}
