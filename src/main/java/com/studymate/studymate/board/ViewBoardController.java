package com.studymate.studymate.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import com.studymate.studymate.dto.BoardResponse; // BoardResponse 임포트 필요
import lombok.RequiredArgsConstructor; // 주입을 위해 임포트
import org.springframework.ui.Model; // Model 임포트 필요
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    // ⭐️ 게시글 상세 조회 View (조회수 중복 방지 쿠키 적용 및 상태 보존)
    // GET /board/{id}
    // ===============================================
    @GetMapping("/{id}")
    public String getBoardDetail(
            @PathVariable Long id,
            @RequestParam(required = false) Integer page,           // 현재 페이지 번호 수신
            @RequestParam(required = false) String searchType,    // 검색 조건 수신
            @RequestParam(required = false) String searchKeyword,   // 검색어 수신
            HttpServletRequest request,   // ⭐️ 쿠키 조회를 위해 추가
            HttpServletResponse response, // ⭐️ 쿠키 생성을 위해 추가
            Model model
    ) {
        // 1. 쿠키를 통한 조회수 어뷰징(새로고침 연타) 방지 로직
        Cookie[] cookies = request.getCookies();
        Cookie viewedCookie = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("viewedBoards".equals(cookie.getName())) {
                    viewedCookie = cookie;
                    break;
                }
            }
        }

        // 해당 글을 본 기록이 없는 경우에만 조회수를 1 올리고 쿠키 갱신
        if (viewedCookie != null) {
            if (!viewedCookie.getValue().contains("[" + id + "]")) {
                boardService.incrementViewCount(id);
                viewedCookie.setValue(viewedCookie.getValue() + "[" + id + "]");
                viewedCookie.setPath("/");
                viewedCookie.setMaxAge(60 * 60 * 24); // 24시간 유지
                response.addCookie(viewedCookie);
            }
        } else {
            boardService.incrementViewCount(id);
            Cookie newCookie = new Cookie("viewedBoards", "[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24); // 24시간 유지
            response.addCookie(newCookie);
        }

        // 2. 게시글 정보 조회 (조회수 증가 후 가져오므로 최신 viewCount가 DTO에 담김)
        // try-catch 없이 정상 흐름만 작성 (게시글이 없으면 Service의 orElseThrow 예외가 발생하여 전역 핸들러로 전달됨)
        // Service를 호출하여 게시글 정보를 가져옵니다.
        BoardResponse board = boardService.getBoardById(id);

        // View로 전달할 데이터를 Model에 담습니다.
        model.addAttribute("board", board);

        // 목록 복귀 시 사용할 상태값 저장
        model.addAttribute("page", page);
        model.addAttribute("searchType", searchType);       // 목록 복귀 시 사용할 상태값 저장
        model.addAttribute("searchKeyword", searchKeyword);

        // Thymeleaf 템플릿 경로: src/main/resources/templates/board/detail.html
        return "board/detail"; // detail.html 템플릿 반환

    }

    // ===============================================
    // 게시글 수정 폼 View (try-catch 제거 -> @ControllerAdvice에 위임)
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
