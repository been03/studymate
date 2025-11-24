package com.studymate.studymate.board;

import com.studymate.studymate.dto.BoardRequest;
import com.studymate.studymate.dto.BoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller; // View 연동을 위해 Controller 사용
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

// @RestController // 기존 REST API 컨트롤러
@Controller // View 리다이렉션을 위해 @Controller로 변경
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    // ===========================================
    // 1. 게시글 생성 (Create) - POST /api/board
    // ===========================================
    @PostMapping
    public String createBoard( // String 반환: View 리다이렉션
                               BoardRequest request, // @RequestBody 제거: 폼 데이터 수신
                               @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();

        boardService.createBoard(request, email); // 게시글 DB 저장

        /* 이전 REST API 응답 코드:
        BoardResponse response = boardService.createBoard(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
        */

        // 저장 완료 후, 목록 페이지로 리다이렉트
        return "redirect:/";
    }

    // ⭐️ 참고: 나머지 GET, PUT, DELETE 메서드는 @ResponseBody를 추가해야 합니다.
    // View를 반환하지 않고 JSON 데이터를 반환해야 하기 때문입니다.

    // ===========================================
    // 2. 게시글 상세 조회 (Read - Single) API - GET /api/board/{id}
    // ===========================================
    @GetMapping("/{id}")
    @ResponseBody // @Controller 상태에서 JSON 응답을 위해 사용
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable Long id) {
        try {
            // // BoardService를 호출하여 특정 ID의 게시글을 조회합니다.
           // BoardResponse response = boardService.getBoardById(id);

            // // HTTP 상태 코드 200 OK와 함께 응답을 반환합니다.
            //return ResponseEntity.ok(response);
            return ResponseEntity.ok(boardService.getBoardById(id));
        } catch (Exception e) {
            // 게시글을 찾을 수 없는 경우 404 Not Found를 반환합니다.
            return ResponseEntity.notFound().build();
        }
    }

    // ===========================================
    // 3. 게시글 목록 조회 (Read - List) API - GET /api/board
    // ===========================================
    @GetMapping
    @ResponseBody // @Controller 상태에서 JSON 응답을 위해 사용
    public ResponseEntity<Page<BoardResponse>> getAllBoards(
            // ⭐️ @PageableDefault를 사용하여 기본값 설정
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        // BoardService를 호출하여 페이징된 게시글 목록을 조회합니다.
        Page<BoardResponse> response = boardService.getAllBoards(pageable);

        // HTTP 상태 코드 200 OK와 함께 응답을 반환합니다.
        return ResponseEntity.ok(response);
    }

    // ===========================================
    // 4. 게시글 수정 (Update) API - PUT /api/board/{id}
    // ===========================================
    @PutMapping("/{id}")
    // @ResponseBody // 리다이렉션을 위해 제거
    public String updateBoard( // 반환 타입을 String으로 변경하여 리다이렉트 가능하게 함
            @PathVariable Long id, // @RequestBody 제거! 폼 데이터(title, content)를 받기 위함.
             BoardRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String email = userDetails.getUsername();

            // Service를 호출하여 게시글을 업데이트합니다.
            boardService.updateBoard(id, request, email);

            // ⭐️ 수정 성공 후, 해당 게시글의 상세 페이지로 리다이렉트합니다.
            return "redirect:/board/" + id;

        } catch (AccessDeniedException e) {
            // 권한이 없는 경우 (403 Forbidden)
            // 리다이렉트 대신 에러 메시지를 보여주는 페이지로 보낼 수 있으나, 여기서는 상세 페이지로 리다이렉트 (추후 개선 가능)
            return "redirect:/board/" + id + "?error=forbidden";
        } catch (NoSuchElementException e) {
            // 게시글이 없는 경우 (404 Not Found)
            return "redirect:/"; // 목록으로 리다이렉트
        }
    }

    // ===========================================
    // 5. 게시글 삭제 (Delete) API - DELETE /api/board/{id}
    // ===========================================
    @DeleteMapping("/{id}")
    // @ResponseBody // 폼 제출 후 리다이렉트를 위해 @ResponseBody를 제거
    public String deleteBoard( // 반환 타입을 String으로 변경
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String email = userDetails.getUsername();
            boardService.deleteBoard(id, email);

            // ⭐️ 삭제 성공 시, 목록 페이지로 리다이렉트
            return "redirect:/";

        } catch (AccessDeniedException e) {
            // 권한이 없는 경우 (403 Forbidden)
            return "redirect:/board/" + id + "?error=denied";
        } catch (NoSuchElementException e) {
            // 게시글이 없는 경우 (404 Not Found)
            return "redirect:/";
        }
    }
}
