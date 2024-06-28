package com.test.lecture.lecture.controller.dto;

import com.test.lecture.lecture.domain.LectureApply;
import com.test.lecture.lecture.domain.Schedule;
import com.test.lecture.lecture.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LectureApplyResponse {

    private final boolean isApplied;
    private final User user;
    private final Schedule schedule;

    public LectureApplyResponse(boolean isApplied, User user, Schedule schedule) {
        this.isApplied = isApplied;
        this.user = user;
        this.schedule = schedule;
    }

    public static LectureApplyResponse from(LectureApply lectureApply) {
        return new LectureApplyResponse(
                lectureApply.isApplied(),
                lectureApply.getUser(),
                lectureApply.getSchedule()
        );
    }
}

