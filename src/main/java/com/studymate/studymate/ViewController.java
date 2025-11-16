package com.studymate.studymate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // ⭐️ View(화면)을 반환하므로 @Controller를 사용합니다.
public class ViewController {

    // http://localhost:8080/login 주소로 접근하면 login.html 파일을 반환
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // src/main/resources/templates/login.html 파일을 찾습니다.
    }
}
