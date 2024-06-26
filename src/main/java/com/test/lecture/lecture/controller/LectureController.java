package com.test.lecture.lecture.controller;

import com.test.lecture.lecture.controller.port.LectureService;
import com.test.lecture.lecture.controller.dto.LectureApplicationResponse;
import com.test.lecture.lecture.controller.dto.LectureResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    /*
        **(기본) 특강 목록 API GET /lectures**
        - 사용자는 각 특강에 신청하기전 목록을 조회해볼 수 있어야 합니다.
          - 추가로 정원이 특강마다 다르다면 어떻게 처리할것인가
         -그냥 요일이랑 과목만 비교
        ( ex) 아래 두개는 같은 수업 취급.
         pk1 토 1시 하헌우
         pk2 토 2시 하헌우)
        -강의와 스케쥴 테이블이 따로 있어야 위 두개 수업이 다른걸 알 수 있을것.(중요)
     */
    @GetMapping
    public ResponseEntity<List<LectureResponse>> getAllLectures() {
        List<LectureResponse> lectures = LectureResponse.from(lectureService.getAllLectures());
        return ResponseEntity.ok(lectures);
    }

    /*
        특강 신청 완료 여부 조회 API **GET /lectures/application/{userId}**
        - 특정 userId 로 특강 신청 완료 여부를 조회하는 API 를 작성합니다.
        - 특강정보와 날짜정보도 같이 파라미터를 넘긴다
         -특강신청 성공했을때 true/false 만 넘기는건 좋은 코드가 아니다.
         -풍부한 데이터를 넘기고 프론트에서 선택해서 쓰게끔 하는게 좋다.
     */
    @GetMapping("/application/{userId}")
    public ResponseEntity<List<LectureApplicationResponse>> getApplicationsByUserId(@PathVariable Long userId) {
        List<LectureApplicationResponse> applicationResponses = LectureApplicationResponse.from(lectureService.getApplicationsByUserId(userId));
        return ResponseEntity.ok(applicationResponses);
    }


    /*
        특강 신청 **API POST /lectures/apply**
        - 특정 userId 로 선착순으로 제공되는 특강을 신청하는 API 를 작성합니다.
        - 동일한 신청자는 한 번의 수강 신청만 성공할 수 있습니다.
        - 예를들어 강의는 선착순 30명이면 30명만 신청 가능합니다.
        - 이미 신청자가 30명이 초과되면 이후 신청자는 요청을 실패합니다.
        - 어떤 유저가 특강을 신청했는지 히스토리를 저장해야한다.
        선착순때문에 낙관적 lock을 안쓰고 , 비관적lock을 사용
     */


}
