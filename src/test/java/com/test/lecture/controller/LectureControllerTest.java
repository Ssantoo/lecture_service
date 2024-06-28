package com.test.lecture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.lecture.common.exception.ResourceNotFoundException;
import com.test.lecture.lecture.controller.LectureController;
import com.test.lecture.lecture.controller.port.LectureService;
import com.test.lecture.lecture.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@WebMvcTest(LectureController.class)
@ExtendWith(MockitoExtension.class)
public class LectureControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    LectureService lectureService;

    @Autowired
    private ObjectMapper objectMapper;

    //강의 목록을 조회한다
    @Test
    void 강의_목록을_조회한다()  throws Exception  {
        //given
        List<Schedule> schedules = Arrays.asList(
                new Schedule(1L, LocalDateTime.of(2024, 6, 20, 10, 0),
                        new Lecture(1L, "스프링", "김선생님", 30, 25, LectureStatus.OPEN)),
                new Schedule(1L, LocalDateTime.of(2024, 6, 20, 10, 0),
                        new Lecture(2L, "tdd수업", "김선생님",  20, 15, LectureStatus.PREPARING)),
                new Schedule(1L, LocalDateTime.of(2024, 6, 20, 10, 0),
                        new Lecture(3L, "알고리즘", "박선생님",  25, 20, LectureStatus.CLOSED))
        );

        //when
        given(lectureService.getAllLectures()).willReturn(schedules);

        //then
        mockMvc.perform(get("/api/lectures")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapper.writeValueAsString(schedules)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        assertThat(schedules).extracting("lecture.lectureName").contains("스프링", "tdd수업", "알고리즘");
        assertThat(schedules).extracting("lecture.totalStudents").contains(30, 20, 25);
    }

    //특강 신청 완료 여부 조회
    @Test
    void 강의_신청_완료_여부_조회() throws Exception {
        // given
        long userId = 1L;
        long scheduleId = 1L;
        User user = new User(userId, "조현재");
        Schedule schedule = new Schedule(1L, LocalDateTime.of(2024, 6, 20, 10, 0),
                new Lecture(1L, "스프링", "김선생님", 30, 25, LectureStatus.OPEN));
        LectureApply lectureApply = LectureApply.update(true, user, schedule);

        // when
        given(lectureService.checkUserLectureApplication(userId, scheduleId)).willReturn(lectureApply);

        // then
        mockMvc.perform(get("/api/lectures/application/{userId}/{scheduleId}", userId, scheduleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applied").value(true))
                .andExpect(jsonPath("$.user.id").value(userId))
                .andExpect(jsonPath("$.user.name").value("조현재"))
                .andExpect(jsonPath("$.schedule.id").value(scheduleId))
                .andExpect(jsonPath("$.schedule.lecture.lectureName").value("스프링"));
    }

    // userId가 null인 경우
    @Test
    void 유저ID_null일_때_에러를_던진다() throws Exception {
        mockMvc.perform(get("/api/lectures/application/null/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // lectureId가 null인 경우
    @Test
    void 강의ID_null일_때_에러를_던진다() throws Exception {
        mockMvc.perform(get("/api/lectures/application/1/null")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    //수강신청
    @Test
    void 수강신청을_성공한다() throws Exception {
        //given
        User user = new User(1L, "조현재");
        Schedule schedule = new Schedule(1L, LocalDateTime.now(), new Lecture(1L, "스프링강의", "김선생님", 30, 10, LectureStatus.OPEN));
        LectureApply lectureApply = LectureApply.create(user, schedule);

        given(lectureService.applyLecture(anyLong(), anyLong())).willReturn(lectureApply);

        //when
        //then
        mockMvc.perform(post("/api/lectures/apply")
                        .param("userId", "1")
                        .param("scheduleId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }




}
