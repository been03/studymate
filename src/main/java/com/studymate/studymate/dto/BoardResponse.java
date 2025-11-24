package com.studymate.studymate.dto;

import com.studymate.studymate.board.Board;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class BoardResponse {

    private final Long id;          // 게시글 ID
    private final String title;     // 제목
    private final String content;   // 내용
    private final String authorEmail; // ⭐️ 작성자의 이메일 (작성자 식별용)
    private final LocalDateTime createdAt; // 작성 일시

    // Board 엔티티를 받아 Response DTO로 변환하는 생성자
    public BoardResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        // ⭐️ N:1 관계를 통해 Board 엔티티의 author(User) 정보에서 이메일을 가져옵니다.
        this.authorEmail = board.getAuthor().getEmail();
        this.createdAt = board.getCreatedAt();
    }
}
