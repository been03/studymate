package com.studymate.studymate.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.studymate.studymate.dto.BoardResponse; // BoardResponse 임포트 필요
import lombok.RequiredArgsConstructor; // 주입을 위해 임포트
import org.springframework.ui.Model; // Model 임포트 필요
import org.springframework.web.bind.annotation.PathVariable;

@Controller // View를 반환하므로 @Controller를 사용합니다.
@RequiredArgsConstructor // Service 주입을 위해 추가
@RequestMapping("/board")
public class ViewBoardController {

    private final BoardService boardService; // Service 주입

    // GET /board/write 요청 처리 (글쓰기 폼)
    @GetMapping("/write")
    public String writeForm() {
        // Thymeleaf 템플릿 경로: src/main/resources/templates/board/write.html
        return "board/write";
    }

    // ===============================================
    // ⭐️ 게시글 상세 조회 View (검색 유형, 키워드, 페이징 상태 보존)
    // GET /board/{id}
    // ===============================================
    @GetMapping("/{id}")
    public String getBoardDetail(
            @PathVariable Long id,
            @RequestParam(required = false) Integer page,           // 현재 페이지 번호 수신
            @RequestParam(required = false) String searchType,    // ⭐️ 추가: 검색 조건 수신
            @RequestParam(required = false) String searchKeyword,   // 검색어 수신
            Model model
    ) {
        // try-catch 없이 정상 흐름만 작성 (게시글이 없으면 Service의 orElseThrow 예외가 발생하여 전역 핸들러로 전달됨)
        // Service를 호출하여 게시글 정보를 가져옵니다.
        BoardResponse board = boardService.getBoardById(id);

        // View로 전달할 데이터를 Model에 담습니다.
        model.addAttribute("board", board);

        // 목록 복귀 시 사용할 상태값 저장
        model.addAttribute("page", page);
        model.addAttribute("searchType", searchType);       // ⭐️ 추가: 목록 복귀 시 사용할 상태값 저장
        model.addAttribute("searchKeyword", searchKeyword);

        // Thymeleaf 템플릿 경로: src/main/resources/templates/board/detail.html
        return "board/detail"; // detail.html 템플릿 반환

    }

    // ===============================================
    // ⭐️ 게시글 수정 폼 View (try-catch 제거 -> @ControllerAdvice에 위임)
    // GET /board/edit/{id}
    // ===============================================
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        // try-catch 제거
        // 1. Service를 호출하여 수정할 게시글 정보를 가져옵니다.
        BoardResponse board = boardService.getBoardById(id);

        // 2. View로 전달할 데이터를 Model에 담습니다.
        model.addAttribute("board", board);

        // 3. Thymeleaf 템플릿 경로: src/main/resources/templates/board/edit.html
        return "board/edit";


    }
}
