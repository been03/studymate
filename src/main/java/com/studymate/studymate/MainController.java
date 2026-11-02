package com.studymate.studymate;

import com.studymate.studymate.board.BoardService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam; // @RequestParam 임포트

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BoardService boardService; // BoardService 주입

    // 루트 경로('/')로 접근 시, 게시글 목록을 가져와 index.html로 전달
    @GetMapping("/")
    public String index(
            // searchKeyword 파라미터를 받도록 추가
            @RequestParam(required = false) String searchKeyword,
            Model model, // View로 데이터를 전달하기 위해 Model 객체 사용
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        // Service 호출 시 searchKeyword 파라미터 전달
        model.addAttribute("boards", boardService.getAllBoards(searchKeyword, pageable));

        // 현재 검색어를 View에 다시 전달하여 검색창에 유지되도록 추가
        model.addAttribute("searchKeyword", searchKeyword);

        return "index"; // index.html 템플릿 반환
    }

    // 로그인 페이지는 기존대로 ViewController가 처리하도록 두거나, 여기에 통합할 수 있습니다.
    // 현재 구조에서는 ViewController가 있으므로 /login 요청은 그대로 두겠습니다.
}