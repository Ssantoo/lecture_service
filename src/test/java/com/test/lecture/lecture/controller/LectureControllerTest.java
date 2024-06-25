package com.test.lecture.lecture.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.lecture.lecture.controller.port.LectureService;
import com.test.lecture.lecture.domain.Lecture;
import com.test.lecture.lecture.domain.LectureStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;


@AutoConfigureMockMvc
@WebMvcTest(LectureController.class)
@ExtendWith(MockitoExtension.class)
//@SqlGroup({
//        @Sql(value = "/sql/lecture-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
//        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//})
public class LectureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    LectureService lectureService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void 수강_목록_조회한다() throws Exception {
        //given
        List<Lecture> lectures = Arrays.asList(
                new Lecture(1L, "스프링", 30, 0, LocalDateTime.of(2024, 6, 13, 10, 0), LectureStatus.PREPARING),
                new Lecture(2L, "tdd수업", 20, 20, LocalDateTime.of(2024, 6, 14, 14, 0), LectureStatus.OPEN),
                new Lecture(3L, "알고리즘", 25, 25, LocalDateTime.of(2024, 6, 10, 9, 0), LectureStatus.CLOSED)
        );
        given(lectureService.getAllLectures()).willReturn(lectures);

        //when
        //then
        mockMvc.perform(get("/api/lectures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("스프링"))
                .andExpect(jsonPath("$[0].seat").value(30))
                .andExpect(jsonPath("$[0].currentSeat").value(0))
                .andExpect(jsonPath("$[0].lectureTime").value("2024-06-13T10:00:00"))
                .andExpect(jsonPath("$[0].lectureStatus").value("PREPARING"))
                .andExpect(jsonPath("$[1].name").value("tdd수업"))
                .andExpect(jsonPath("$[1].seat").value(20))
                .andExpect(jsonPath("$[1].currentSeat").value(20))
                .andExpect(jsonPath("$[1].lectureTime").value("2024-06-14T14:00:00"))
                .andExpect(jsonPath("$[1].lectureStatus").value("OPEN"))
                .andExpect(jsonPath("$[2].name").value("알고리즘"))
                .andExpect(jsonPath("$[2].seat").value(25))
                .andExpect(jsonPath("$[2].currentSeat").value(25))
                .andExpect(jsonPath("$[2].lectureTime").value("2024-06-10T09:00:00"))
                .andExpect(jsonPath("$[2].lectureStatus").value("CLOSED"));
    }




}
