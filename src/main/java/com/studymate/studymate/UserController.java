package com.studymate.studymate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping; // GET에서 POST로 변경
import org.springframework.web.bind.annotation.RequestBody; // JSON 데이터를 받기 위한 어노테이션 추가
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.RequiredArgsConstructor;
import  org.springframework.http.ResponseEntity;

import com.studymate.studymate.dto.UserJoinRequest; // DTO 임포트 추가
import com.studymate.studymate.dto.UserLoginRequest;

@Controller
@ResponseBody
@RequiredArgsConstructor // 추가
public class UserController {

    private final UserService userService;

    @PostMapping("/user") // POST 요청만 받도록 변경
    public String signUp(@RequestBody UserJoinRequest request) { // DTO를 사용하여 요청 받기

        Long savedUserId = userService.join(request);

        return "New user created with ID: " + savedUserId;
    }

    // 로그인 요청 처리
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest request) {
        // 1. UserService의 로그인 로직을 호출
        Long userId = userService.login(request);

        // 2. 로그인 성공 시 응답 (실제로는 세션, JWT 등을 반환하지만 현재는 ID로 성공 확인)
        return ResponseEntity.ok("User logged in successfully with ID: " + userId);
    }

}
