package com.studymate.studymate;

import com.studymate.studymate.board.BoardService;
import com.studymate.studymate.dto.BoardResponse; // ⭐️ BoardResponse 임포트 추가

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page; // ⭐️ Page 임포트 추가
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
            @RequestParam(required = false, defaultValue = "titleOrContent") String searchType, // 검색 조건 수신
            @RequestParam(required = false) String searchKeyword,                               // 검색어 수신
            Model model, // View로 데이터를 전달하기 위해 Model 객체 사용
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        // ⭐️ searchType, searchKeyword, pageable 3개를 모두 전달하여 게시글 조회
        Page<BoardResponse> boards = boardService.getAllBoards(searchType, searchKeyword, pageable);

        // 조회 결과를 Model에 담아 View로 전달
        model.addAttribute("boards", boards);
        model.addAttribute("searchType", searchType);       // 화면 드롭다운 선택 상태 유지용
        model.addAttribute("searchKeyword", searchKeyword); // 검색창 입력값 유지용

        return "index"; // index.html 템플릿 반환
    }

    // 로그인 페이지는 기존대로 ViewController가 처리하도록 두거나, 여기에 통합할 수 있습니다.
    // 현재 구조에서는 ViewController가 있으므로 /login 요청은 그대로 두겠습니다.
}
