package com.studymate.studymate;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping; // GET에서 POST로 변경
import org.springframework.web.bind.annotation.RequestBody; // JSON 데이터를 받기 위한 어노테이션 추가
import lombok.RequiredArgsConstructor;
import  org.springframework.http.ResponseEntity;

import com.studymate.studymate.dto.UserJoinRequest; // DTO 임포트 추가
import com.studymate.studymate.dto.UserLoginRequest;

@RestController // ⭐️ @Controller + @ResponseBody의 역할을 합니다.
@RequiredArgsConstructor // 추가
public class UserController {

    private final UserService userService;

    //회원가입 API 구현
    @PostMapping("/user")
    public ResponseEntity<String> join(@RequestBody UserJoinRequest request) {
        userService.join(request); // ⭐️ 이 join 메서드 안에서 비밀번호가 BCrypt로 암호화됨
        return ResponseEntity.ok("User joined successfully!");
    }

    /*
    // Spring Security 도입 전, Postman 등으로 로그인 로직(userService.login)의 작동을
    // 확인하기 위해 잠시 사용했던 테스트용 API 엔드포인트.
    // 이제 Spring Security가 폼 로그인 처리를 담당하므로 주석 처리(혹은 삭제)함.
    // 로그인 요청 처리
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest request) {
        // 1. UserService의 로그인 로직을 호출
        Long userId = userService.login(request);

        // 2. 로그인 성공 시 응답 (실제로는 세션, JWT 등을 반환하지만 현재는 ID로 성공 확인)
        return ResponseEntity.ok("User logged in successfully with ID: " + userId);
    }
     */
}
