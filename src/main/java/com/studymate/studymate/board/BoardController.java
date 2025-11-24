package com.studymate.studymate.board;

import com.studymate.studymate.dto.BoardRequest;
import com.studymate.studymate.dto.BoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;

import java.util.NoSuchElementException;

@RestController // ⭐️ RESTful API 컨트롤러임을 명시
@RequiredArgsConstructor
@RequestMapping("/api/board") // ⭐️ 기본 URL 경로 설정
public class BoardController {

    private final BoardService boardService;

    // ===========================================
    // 1. 게시글 생성 (Create) API - POST /api/board
    // ===========================================
    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(
            @RequestBody BoardRequest request,
            @AuthenticationPrincipal UserDetails userDetails // ⭐️ 현재 로그인 사용자 정보 주입
    ) {
        // UserDetails에서 사용자의 이메일(username)을 가져옵니다.
        String email = userDetails.getUsername();

        // BoardService를 호출하여 게시글을 생성하고 응답 DTO를 받습니다.
        BoardResponse response = boardService.createBoard(request, email);

        // HTTP 상태 코드 201 Created와 함께 응답을 반환합니다.
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ===========================================
    // 2. 게시글 상세 조회 (Read - Single) API - GET /api/board/{id}
    // ===========================================
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable Long id) {
        try {
            // BoardService를 호출하여 특정 ID의 게시글을 조회합니다.
            BoardResponse response = boardService.getBoardById(id);

            // HTTP 상태 코드 200 OK와 함께 응답을 반환합니다.
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 게시글을 찾을 수 없는 경우 404 Not Found를 반환합니다.
            return ResponseEntity.notFound().build();
        }
    }

    // ===========================================
    // 3. 게시글 목록 조회 (Read - List) API - GET /api/board
    // ===========================================
    @GetMapping
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
    public ResponseEntity<BoardResponse> updateBoard(
            @PathVariable Long id,
            @RequestBody BoardRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String email = userDetails.getUsername();
            BoardResponse response = boardService.updateBoard(id, request, email);
            return ResponseEntity.ok(response);
        } catch (AccessDeniedException e) {
            // 권한이 없는 경우 403 Forbidden 응답 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (NoSuchElementException e) {
            // 게시글이 없는 경우 404 Not Found 응답 반환
            return ResponseEntity.notFound().build();
        }
    }

    // ===========================================
    // 5. 게시글 삭제 (Delete) API - DELETE /api/board/{id}
    // ===========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String email = userDetails.getUsername();
            boardService.deleteBoard(id, email);
            // 삭제 성공 시 204 No Content 반환
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            // 권한이 없는 경우 403 Forbidden 응답 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (NoSuchElementException e) {
            // 게시글이 없는 경우 404 Not Found 응답 반환
            return ResponseEntity.notFound().build();
        }
    }
}
