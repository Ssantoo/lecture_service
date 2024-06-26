package com.test.lecture.lecture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.lecture.common.domain.exception.ResourceNotFoundException;
import com.test.lecture.lecture.controller.dto.LectureApplicationResponse;
import com.test.lecture.lecture.controller.port.LectureService;
import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.LectureApplication;
import com.test.lecture.lecture.domain.Schedule;
import com.test.lecture.lecture.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    public void 수강_목록_조회한다() throws Exception {
        //given
//        List<Lecture> lectures = Arrays.asList(
//                Lecture.builder()
//                        .id(1L)
//                        .name("스프링")
//                        .instructor("김선생님")
//                        .maxCapacity(30)
//                        .schedules(Arrays.asList(
//                                Schedule.builder()
//                                        .id(1L)
//                                        .time(LocalDateTime.of(2024, 6, 13, 10, 0))
//                                        .build()
//                        ))
//                        .build(),
//                Lecture.builder()
//                        .id(2L)
//                        .name("tdd수업")
//                        .instructor("김선생님")
//                        .maxCapacity(20)
//                        .schedules(Arrays.asList(
//                                Schedule.builder()
//                                        .id(2L)
//                                        .time(LocalDateTime.of(2024, 6, 14, 14, 0))
//                                        .build()
//                        ))
//                        .build(),
//                Lecture.builder()
//                        .id(3L)
//                        .name("알고리즘")
//                        .instructor("박선생님")
//                        .maxCapacity(25)
//                        .schedules(Arrays.asList(
//                                Schedule.builder()
//                                        .id(3L)
//                                        .time(LocalDateTime.of(2024, 6, 10, 9, 0))
//                                        .build()
//                        ))
//                        .build()
//        );

        List<Lecture> lectures = Arrays.asList(
                new Lecture(1L, "스프링", "김선생님", 30, Arrays.asList(
                        new Schedule(1L, LocalDateTime.of(2024, 6, 13, 10, 0), null))),
                new Lecture(2L, "tdd수업", "김선생님", 20, Arrays.asList(
                        new Schedule(2L, LocalDateTime.of(2024, 6, 14, 14, 0), null))),
                new Lecture(3L, "알고리즘", "박선생님", 25, Arrays.asList(
                        new Schedule(3L, LocalDateTime.of(2024, 6, 10, 9, 0), null)))
        );

        //when
        given(lectureService.getAllLectures()).willReturn(lectures);

        //then
        mockMvc.perform(get("/api/lectures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lectures)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("스프링"))
                .andExpect(jsonPath("$[0].maxCapacity").value(30))
                .andExpect(jsonPath("$[0].schedules[0].time").value("2024-06-13T10:00:00"))
                .andExpect(jsonPath("$[1].name").value("tdd수업"))
                .andExpect(jsonPath("$[1].maxCapacity").value(20))
                .andExpect(jsonPath("$[1].schedules[0].time").value("2024-06-14T14:00:00"))
                .andExpect(jsonPath("$[2].name").value("알고리즘"))
                .andExpect(jsonPath("$[2].maxCapacity").value(25))
                .andExpect(jsonPath("$[2].schedules[0].time").value("2024-06-10T09:00:00"));

        assertThat(lectures).hasSize(3);
        assertThat(lectures).extracting("name").contains("스프링", "tdd수업", "알고리즘");
        assertThat(lectures).extracting("maxCapacity").contains(30, 20, 25);
    }

    @Test
    void 사용자가_특정_강의에_신청한_경우_강의_신청_목록을_조회한다() throws Exception {
        //given
        long userId = 1L;
        User user = User.builder().id(userId).name("조현재").build();

        LectureApplication app1 = LectureApplication.builder()
                .id(1L)
                .user(user)
                .lecture(new Lecture(1L, "스프링", "김선생님", 30, Arrays.asList(
                        new Schedule(1L, LocalDateTime.of(2024, 6, 13, 10, 0), null))))
                .schedule(new Schedule(1L, LocalDateTime.of(2024, 6, 13, 10, 0), null))
                .applicationDate(LocalDate.now())
                .build();

        LectureApplication app2 = LectureApplication.builder()
                .id(2L)
                .user(user)
                .lecture(new Lecture(2L, "tdd수업", "김선생님", 20, Arrays.asList(
                        new Schedule(2L, LocalDateTime.of(2024, 6, 14, 14, 0), null))))
                .schedule(new Schedule(2L, LocalDateTime.of(2024, 6, 14, 14, 0), null))
                .applicationDate(LocalDate.now())
                .build();

        List<LectureApplication> applications = Arrays.asList(app1, app2);
        //when
        given(lectureService.getApplicationsByUserId(userId)).willReturn(applications);


        //then
        mockMvc.perform(get("/api/lectures/application/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].lectureId").value(1))
                .andExpect(jsonPath("$[0].lectureName").value("스프링"))
                .andExpect(jsonPath("$[0].teacher").value("김선생님"))
                .andExpect(jsonPath("$[0].time").value("2024-06-13T10:00:00"))
                .andExpect(jsonPath("$[1].lectureId").value(2))
                .andExpect(jsonPath("$[1].lectureName").value("tdd수업"))
                .andExpect(jsonPath("$[1].teacher").value("김선생님"))
                .andExpect(jsonPath("$[1].time").value("2024-06-14T14:00:00"));


    }

    @Test
    void 사용자가_존재하지_않을_경우_히스토리_조회_실패한다() throws Exception {
        //given
        long userId = 1L;

        given(lectureService.getApplicationsByUserId(userId)).willThrow(new ResourceNotFoundException("User", userId));

        //when
        //then
        mockMvc.perform(get("/api/lectures/application/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("User에서 ID 1를 찾을 수 없습니다."));
    }
}
