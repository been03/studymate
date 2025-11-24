package com.studymate.studymate.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // ⭐️ View를 반환하므로 @Controller를 사용합니다.
@RequestMapping("/board")
public class ViewBoardController {

    // GET /board/write 요청 처리
    @GetMapping("/write")
    public String writeForm() {
        // Thymeleaf 템플릿 경로: src/main/resources/templates/board/write.html
        return "board/write";
    }
}
