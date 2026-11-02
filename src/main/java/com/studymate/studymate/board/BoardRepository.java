package com.studymate.studymate.board;

import com.studymate.studymate.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

// Board 엔티티와 ID 타입(Long)을 상속받아 기본 CRUD 기능을 자동 제공받습니다.
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 게시글 제목(title) 또는 내용(content)에서 검색 키워드를 포함하는 게시글을 찾습니다.
    // Pageable을 사용하여 페이징 처리도 함께 진행합니다.
    Page<Board> findByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword, Pageable pageable);
}
