package com.studymate.studymate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping; // GET에서 POST로 변경
import org.springframework.web.bind.annotation.RequestBody; // JSON 데이터를 받기 위한 어노테이션 추가
import org.springframework.web.bind.annotation.ResponseBody;
import  org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.studymate.studymate.dto.UserJoinRequest; // DTO 임포트 추가


@Controller // ⭐️ 리다이렉트(redirect:/) 처리를 위해 @RestController 대신 @Controller 사용
@RequiredArgsConstructor // 추가
public class UserController {

    private final UserService userService;

    //회원가입 API 구현 (기존 유지, JSON 응답을 위해 @ResponseBody 추가)
    @PostMapping("/user")
    @ResponseBody
    public ResponseEntity<String> join(@RequestBody UserJoinRequest request) {
        userService.join(request); // ⭐️ 이 join 메서드 안에서 비밀번호가 BCrypt로 암호화됨
        return ResponseEntity.ok("User joined successfully!");
    }

    // ⭐️ 회원 탈퇴 및 자동 로그아웃 처리
    @PostMapping("/user/withdraw")
    public String withdraw(Authentication authentication,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        if (authentication != null) {
            String email = authentication.getName(); // 현재 로그인한 사용자의 email
            userService.withdraw(email);             // DB Soft Delete 처리 (isDeleted = true)

            // Spring Security 세션 무효화 및 로그아웃 핸들러 동작
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        // 탈퇴 완료 후 메인 페이지로 이동
        return "redirect:/";
    }

}
