package com.test.lecture.lecture.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LectureApply {

    private final boolean isApplied;
    private final User user;
    private final Schedule schedule;



    @Builder
    public LectureApply(boolean isApplied, User user, Schedule schedule) {
        this.isApplied = isApplied;
        this.user = user;
        this.schedule = schedule;

    }

    public static LectureApply update(boolean isApplied, User user, Schedule schedule) {
        return LectureApply.builder()
                .isApplied(isApplied)
                .user(user)
                .schedule(schedule)
                .build();
    }

    public static LectureApply create(User user, Schedule schedule) {
        return LectureApply.builder()
                .isApplied(true)
                .user(user)
                .schedule(schedule)
                .build();
    }

}
