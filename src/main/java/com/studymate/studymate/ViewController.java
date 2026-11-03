package com.studymate.studymate;

import com.studymate.studymate.dto.UserJoinRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    // ⭐️ 2. 회원가입 화면 열기 (Thymeleaf th:object 바인딩을 위한 빈 DTO 전달)
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userJoinRequest", new UserJoinRequest());
        return "register";
    }

    // ⭐️ 3. 회원가입 폼 데이터 검증 및 처리
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userJoinRequest") UserJoinRequest request,
                               BindingResult bindingResult,
                               Model model) {

        // 1. @Valid 검증 실패 시(빈값, 글자 수 미달, 이메일 형식 오류 등)
        if (bindingResult.hasErrors()) {
            return "register"; // 입력값을 유지한 채 회원가입 화면 복귀 (각 필드 아래 빨간 에러 메시지 표시)
        }

        // 2. 비즈니스 로직 검증 (중복 이메일 체크 등)
        try {
            userService.join(request); // 기존에 만들어둔 join 로직 호출 (암호화 및 DB 저장)
            return "redirect:/login"; // 성공 시 로그인 화면으로 이동
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
}
