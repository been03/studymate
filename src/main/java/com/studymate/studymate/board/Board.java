package com.studymate.studymate.board;

import com.studymate.studymate.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor // ⭐️ 기본 생성자 자동 생성
@Entity // ⭐️ JPA 엔티티임을 명시
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시글 ID (Primary Key)

    @Column(nullable = false)
    private String title; // 게시글 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 게시글 내용 (긴 텍스트를 위해 TEXT 타입 사용)

    @ManyToOne(fetch = FetchType.LAZY) // ⭐️ N:1 관계 설정 (게시글 N개, 사용자 1명)
    @JoinColumn(name = "user_id", nullable = false) // user_id 컬럼으로 매핑
    private User author; // 작성자 정보

    @CreatedDate
    @Column(updatable = false) // ⭐️ 생성일자는 업데이트되지 않음
    private LocalDateTime createdAt; // 생성 일시

    // 조회수 등의 필드는 필요에 따라 추가할 수 있습니다.

    @Builder
    public Board(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }

    // ⭐️ 게시글 수정을 위한 메서드
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
