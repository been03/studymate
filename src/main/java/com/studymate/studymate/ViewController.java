package com.studymate.studymate;

import com.studymate.studymate.dto.UserJoinRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller // ⭐️ View(화면)을 반환하므로 @Controller를 사용합니다.
@RequiredArgsConstructor
public class ViewController {

    private final UserService userService; // 회원가입 비즈니스 로직 호출용 주입

    // 1. 기존 로그인 화면
    // http://localhost:8080/login 주소로 접근하면 login.html 파일을 반환
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // src/main/resources/templates/login.html 파일을 찾습니다.
    }

    // ⭐️ 2. 신규: 회원가입 화면 열기
    @GetMapping("/register")
    public String registerPage() {
        return "register"; // templates/register.html 반환
    }

    // ⭐️ 3. 신규: 회원가입 폼(POST) 데이터 처리
    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserJoinRequest request, Model model) {
        try {
            userService.join(request); // 기존에 만들어둔 join 로직 호출 (암호화 및 DB 저장)
            return "redirect:/login";  // 가입 완료 후 바로 로그인 화면으로 이동
        } catch (IllegalArgumentException e) {
            // 중복 이메일 등 예외 발생 시 화면에 에러 메시지를 넘겨줌
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
}
