package com.studymate.studymate.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    // ⭐️ 게시글 상세 조회 View 기능 추가
    // GET /board/{id} 요청 처리
    // ===============================================
    @GetMapping("/{id}")
    public String getBoardDetail(@PathVariable Long id, Model model) {
        try {
            // Service를 호출하여 게시글 정보를 가져옵니다.
            BoardResponse board = boardService.getBoardById(id);

            // View로 전달할 데이터를 Model에 담습니다.
            model.addAttribute("board", board);

            // Thymeleaf 템플릿 경로: src/main/resources/templates/board/detail.html
            return "board/detail"; // ⭐️ detail.html 템플릿 반환

        } catch (Exception e) {
            // 게시글을 찾지 못하면 목록으로 리다이렉트 (에러 처리)
            return "redirect:/";
        }
    }

    // ===============================================
// ⭐️ 게시글 수정 폼 View 기능 추가
// GET /board/edit/{id} 요청 처리
// ===============================================
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            // 1. Service를 호출하여 수정할 게시글 정보를 가져옵니다.
            BoardResponse board = boardService.getBoardById(id);

            // 2. View로 전달할 데이터를 Model에 담습니다.
            model.addAttribute("board", board);

            // 3. Thymeleaf 템플릿 경로: src/main/resources/templates/board/edit.html
            return "board/edit";

        } catch (Exception e) {
            // 게시글이 없거나 접근 오류 시 목록으로 리다이렉트
            return "redirect:/";
        }
    }
}
